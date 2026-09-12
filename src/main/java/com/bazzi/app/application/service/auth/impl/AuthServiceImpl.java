package com.bazzi.app.application.service.auth.impl;

import com.bazzi.app.application.dto.response.auth.TokenResponse;
import com.bazzi.app.application.exception.InvalidTokenException;
import com.bazzi.app.application.service.auth.AuthService;
import com.bazzi.app.util.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtProvider jwtProvider;
    private final StringRedisTemplate redisTemplate;

    @Value("${app.jwt.refresh-token-expiry}")
    private long refreshTokenExpiry;

    private static final String BLACKLIST_PREFIX = "token:blacklist:";

    @Override
    public TokenResponse refresh(String refreshToken) {
        if (!jwtProvider.isValid(refreshToken)) {
            throw new InvalidTokenException("유효하지 않은 리프레시 토큰입니다.");
        }
        if (isBlacklisted(refreshToken)) {
            throw new InvalidTokenException("이미 로그아웃된 토큰입니다.");
        }
        Long userId = jwtProvider.getUserId(refreshToken);
        String newAccessToken = jwtProvider.createAccessToken(userId);
        String newRefreshToken = jwtProvider.createRefreshToken(userId);

        blacklist(refreshToken);

        return new TokenResponse(newAccessToken, newRefreshToken,
                jwtProvider.getAccessTokenExpiry());
    }

    @Override
    public void logout(String refreshToken) {
        if (jwtProvider.isValid(refreshToken)) {
            blacklist(refreshToken);
        }
    }

    private void blacklist(String token) {
        redisTemplate.opsForValue().set(
                BLACKLIST_PREFIX + token,
                "1",
                Duration.ofSeconds(refreshTokenExpiry)
        );
    }

    private boolean isBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_PREFIX + token));
    }
}
