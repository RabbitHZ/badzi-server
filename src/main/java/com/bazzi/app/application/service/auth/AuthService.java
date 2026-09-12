package com.bazzi.app.application.service.auth;

import com.bazzi.app.application.dto.response.auth.TokenResponse;

public interface AuthService {
    TokenResponse refresh(String refreshToken);
    void logout(String refreshToken);
}
