package com.bazzi.app.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionResponseDto {
    private boolean authenticated;
    private String username;
    private String avatarUrl;
    private String profileUrl;
}
