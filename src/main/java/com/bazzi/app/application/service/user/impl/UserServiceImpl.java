package com.bazzi.app.application.service.user.impl;

import com.bazzi.app.application.dto.response.user.UserProfileResponse;
import com.bazzi.app.application.exception.InvalidTokenException;
import com.bazzi.app.application.service.user.UserService;
import com.bazzi.app.infrastructure.persistence.user.User;
import com.bazzi.app.infrastructure.persistence.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserProfileResponse getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidTokenException("사용자를 찾을 수 없습니다."));
        return new UserProfileResponse(user);
    }
}
