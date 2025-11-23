package com.bazzi.app.application.service;

import com.bazzi.app.application.dto.response.SessionResponseDto;

public interface AuthService {
    SessionResponseDto getSession(String accessToken);
    String exchangeCodeForToken(String code);
}
