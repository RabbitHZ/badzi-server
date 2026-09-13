package com.bazzi.app.interfaces.config.oauth;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 표준 OAuth2 유저 정보를 로드한 뒤, GitHub 로그인에서 이메일이 비공개(primary email
 * 미공개)라 {@code /user} 응답의 email 이 null 인 경우 {@code /user/emails} 를 호출해
 * primary·verified 이메일을 채워 넣는다.
 *
 * <p>GitHub 은 유저가 이메일을 비공개로 설정하면 {@code /user} 의 email 을 null 로 내려주지만,
 * {@code user:email} scope 가 있으면 {@code /user/emails} 에서 primary 이메일을 얻을 수 있다.
 */
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private static final String GITHUB = "github";
    private static final String EMAIL_ATTRIBUTE = "email";

    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
    private final RestClient restClient = RestClient.create();

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Object existingEmail = oAuth2User.getAttributes().get(EMAIL_ATTRIBUTE);

        if (!GITHUB.equals(registrationId) || existingEmail != null) {
            return oAuth2User;
        }

        String primaryEmail = fetchGithubPrimaryEmail(userRequest.getAccessToken().getTokenValue());
        if (primaryEmail == null) {
            return oAuth2User;
        }

        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
        attributes.put(EMAIL_ATTRIBUTE, primaryEmail);

        String nameAttributeKey = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        return new DefaultOAuth2User(
                new SimpleAuthorityMapper().mapAuthorities(oAuth2User.getAuthorities()),
                attributes,
                nameAttributeKey);
    }

    private String fetchGithubPrimaryEmail(String accessToken) {
        try {
            List<Map<String, Object>> emails = restClient.get()
                    .uri("https://api.github.com/user/emails")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<Map<String, Object>>>() {});

            if (emails == null || emails.isEmpty()) {
                return null;
            }

            // 1순위: primary + verified, 2순위: verified, 3순위: 아무거나
            String verifiedFallback = null;
            String anyFallback = null;
            for (Map<String, Object> entry : emails) {
                String email = (String) entry.get("email");
                if (email == null) continue;
                boolean primary = Boolean.TRUE.equals(entry.get("primary"));
                boolean verified = Boolean.TRUE.equals(entry.get("verified"));
                if (primary && verified) return email;
                if (verified && verifiedFallback == null) verifiedFallback = email;
                if (anyFallback == null) anyFallback = email;
            }
            return verifiedFallback != null ? verifiedFallback : anyFallback;
        } catch (Exception e) {
            // 이메일 조회 실패는 로그인 자체를 막지 않는다. 후속 저장 로직에서 처리.
            return null;
        }
    }
}
