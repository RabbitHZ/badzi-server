package com.bazzi.app.application.dto.response.user;

import com.bazzi.app.infrastructure.persistence.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class UserProfileResponse {

    @Schema(description = "사용자 ID")
    private final Long id;

    @Schema(description = "OAuth 제공자")
    private final String provider;

    @Schema(description = "사용자명")
    private final String username;

    @Schema(description = "이메일 (복호화)")
    private final String email;

    @Schema(description = "이름 (복호화)")
    private final String name;

    public UserProfileResponse(User user) {
        this.id = user.getId();
        this.provider = user.getProvider();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.name = user.getName();
    }
}
