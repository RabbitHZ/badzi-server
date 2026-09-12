package com.bazzi.app.auth;

import com.bazzi.app.application.exception.InvalidTokenException;
import com.bazzi.app.application.service.auth.AuthService;
import com.bazzi.app.application.dto.response.auth.TokenResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @Test
    void 토큰_재발급_성공() throws Exception {
        TokenResponse tokenResponse = new TokenResponse("new-access", "new-refresh", 3600L);
        when(authService.refresh(anyString())).thenReturn(tokenResponse);

        mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                Map.of("refreshToken", "valid-refresh-token"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").value("new-access"));
    }

    @Test
    void 유효하지않은_리프레시토큰_401() throws Exception {
        when(authService.refresh(anyString()))
                .thenThrow(new InvalidTokenException("유효하지 않은 리프레시 토큰입니다."));

        mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                Map.of("refreshToken", "bad-token"))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void 로그아웃_성공() throws Exception {
        doNothing().when(authService).logout(anyString());

        mockMvc.perform(post("/api/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                Map.of("refreshToken", "any-refresh-token"))))
                .andExpect(status().isOk());
    }
}
