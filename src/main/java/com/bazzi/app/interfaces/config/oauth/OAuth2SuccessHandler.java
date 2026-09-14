package com.bazzi.app.interfaces.config.oauth;

import com.bazzi.app.infrastructure.persistence.user.User;
import com.bazzi.app.infrastructure.persistence.user.UserRepository;
import com.bazzi.app.util.crypto.HashUtil;
import com.bazzi.app.util.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final HashUtil hashUtil;
    private final JwtProvider jwtProvider;

    @Value("${app.base-url}")
    private String baseUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String registrationId = extractRegistrationId(request);

        String providerId = extractProviderId(oAuth2User, registrationId);
        String email = extractEmail(oAuth2User, registrationId);
        String name = (String) oAuth2User.getAttributes().get("name");

        if (email == null) {
            getRedirectStrategy().sendRedirect(request, response,
                    baseUrl + "/?error=email_required");
            return;
        }

        String emailHash = hashUtil.hash(email);

        User user = userRepository.findByEmailHash(emailHash)
                .orElseGet(() -> userRepository.findByProviderAndProviderId(registrationId, providerId)
                        .orElse(null));

        if (user == null) {
            user = User.builder()
                    .provider(registrationId)
                    .providerId(providerId)
                    .email(email)
                    .emailHash(emailHash)
                    .name(name)
                    .build();
            user = userRepository.save(user);
        } else {
            user.updateProfile(email, emailHash, name, null);
            userRepository.save(user);
        }

        String accessToken = jwtProvider.createAccessToken(user.getId());
        String refreshToken = jwtProvider.createRefreshToken(user.getId());

        String redirectUrl = baseUrl + "/?access_token=" + accessToken
                + "&refresh_token=" + refreshToken;
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }

    private String extractRegistrationId(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String[] parts = uri.split("/");
        return parts[parts.length - 1];
    }

    private String extractProviderId(OAuth2User user, String registrationId) {
        Object id = user.getAttributes().get("id");
        if (id != null) return String.valueOf(id);
        return (String) user.getAttributes().get("sub");
    }

    private String extractEmail(OAuth2User user, String registrationId) {
        String email = (String) user.getAttributes().get("email");
        if (email != null) return email;
        Object emails = user.getAttributes().get("emails");
        if (emails instanceof java.util.List<?> list && !list.isEmpty()) {
            Object first = list.get(0);
            if (first instanceof Map<?, ?> m) return (String) m.get("email");
        }
        return null;
    }
}
