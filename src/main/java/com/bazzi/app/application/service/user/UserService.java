package com.bazzi.app.application.service.user;

import com.bazzi.app.application.dto.response.user.UserProfileResponse;

public interface UserService {
    UserProfileResponse getProfile(Long userId);
}
