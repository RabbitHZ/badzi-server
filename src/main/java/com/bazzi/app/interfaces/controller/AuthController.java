package com.bazzi.app.interfaces.controller;

import com.bazzi.app.application.dto.response.SessionResponseDto;
import com.bazzi.app.application.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Tag(name = "Auth API", description = "GitHub OAuth 인증 API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Value("${spring.security.oauth2.client.registration.github.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.github.scope}")
    private String scope;

    @Value("${spring.security.oauth2.client.registration.github.redirect-uri}")
    private String redirectUri;

    @Value("${app.oauth2.authorized-redirect-uri}")
    private String authorizedRedirectUri;

    @Operation(summary = "GitHub OAuth 시작", description = "GitHub 로그인 페이지로 리다이렉트")
    @GetMapping("/github")
    public void startGitHubOAuth(HttpServletResponse response) throws IOException {
        String authorizationUrl = "https://github.com/login/oauth/authorize"
                + "?client_id=" + clientId
                + "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8)
                + "&scope=" + URLEncoder.encode(scope, StandardCharsets.UTF_8);

        response.sendRedirect(authorizationUrl);
    }

    @Operation(summary = "OAuth 콜백", description = "GitHub OAuth 콜백 처리")
    @GetMapping("/callback")
    public void handleCallback(
            @RequestParam String code,
            HttpSession session,
            HttpServletResponse response
    ) throws IOException {
        String accessToken = authService.exchangeCodeForToken(code);
        session.setAttribute("accessToken", accessToken);

        response.sendRedirect(authorizedRedirectUri);
    }

    @Operation(summary = "세션 조회", description = "현재 로그인된 사용자 정보 조회")
    @GetMapping("/session")
    public ResponseEntity<SessionResponseDto> getSession(HttpSession session) {
        String accessToken = (String) session.getAttribute("accessToken");
        SessionResponseDto sessionResponse = authService.getSession(accessToken);
        return ResponseEntity.ok(sessionResponse);
    }

    @Operation(summary = "로그아웃", description = "현재 세션 종료")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok().build();
    }
}
