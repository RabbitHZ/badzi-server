package com.bazzi.app.auth;

import com.bazzi.app.util.jwt.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtProviderTest {

    private JwtProvider jwtProvider;

    @BeforeEach
    void setUp() {
        jwtProvider = new JwtProvider(
                "test-secret-key-for-testing-only-min-32-chars-long",
                3600L,
                604800L
        );
    }

    @Test
    void 액세스토큰_발급_후_userId_추출() {
        String token = jwtProvider.createAccessToken(42L);
        assertThat(jwtProvider.getUserId(token)).isEqualTo(42L);
    }

    @Test
    void 리프레시토큰_발급_후_userId_추출() {
        String token = jwtProvider.createRefreshToken(99L);
        assertThat(jwtProvider.getUserId(token)).isEqualTo(99L);
    }

    @Test
    void 유효한_토큰_검증() {
        String token = jwtProvider.createAccessToken(1L);
        assertThat(jwtProvider.isValid(token)).isTrue();
    }

    @Test
    void 잘못된_토큰_검증_실패() {
        assertThat(jwtProvider.isValid("invalid.token.here")).isFalse();
    }

    @Test
    void 액세스토큰과_리프레시토큰은_다르다() {
        String access = jwtProvider.createAccessToken(1L);
        String refresh = jwtProvider.createRefreshToken(1L);
        assertThat(access).isNotEqualTo(refresh);
    }
}
