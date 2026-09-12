package com.bazzi.app.application.service.badge.impl;

import com.bazzi.app.application.dto.request.badge.BadgeStyleRequest;
import com.bazzi.app.application.dto.response.badge.BadgeStyleResponse;
import com.bazzi.app.application.exception.ResourceNotFoundException;
import com.bazzi.app.application.service.badge.BadgeStyleService;
import com.bazzi.app.infrastructure.persistence.badge.BadgeStyle;
import com.bazzi.app.infrastructure.persistence.badge.BadgeStyleRepository;
import com.bazzi.app.infrastructure.persistence.user.User;
import com.bazzi.app.infrastructure.persistence.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BadgeStyleServiceImpl implements BadgeStyleService {

    private final BadgeStyleRepository badgeStyleRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<BadgeStyleResponse> getUserStyles(Long userId) {
        return badgeStyleRepository.findAllByUserId(userId).stream()
                .map(BadgeStyleResponse::new)
                .toList();
    }

    @Override
    @Transactional
    public BadgeStyleResponse createStyle(Long userId, BadgeStyleRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));

        BadgeStyle style = BadgeStyle.builder()
                .user(user)
                .name(request.getName())
                .styleType(request.getStyleType())
                .color(request.getColor())
                .label(request.getLabel())
                .icon(request.getIcon())
                .fontSize(request.getFontSize())
                .preset(false)
                .build();

        return new BadgeStyleResponse(badgeStyleRepository.save(style));
    }

    @Override
    @Transactional
    public BadgeStyleResponse updateStyle(Long userId, Long styleId, BadgeStyleRequest request) {
        BadgeStyle style = badgeStyleRepository.findById(styleId)
                .filter(s -> s.getUser() != null && s.getUser().getId().equals(userId))
                .orElseThrow(() -> new ResourceNotFoundException("스타일을 찾을 수 없습니다."));

        style.update(request.getName(), request.getColor(), request.getLabel(),
                request.getIcon(), request.getFontSize());

        return new BadgeStyleResponse(style);
    }

    @Override
    @Transactional
    public void deleteStyle(Long userId, Long styleId) {
        BadgeStyle style = badgeStyleRepository.findById(styleId)
                .filter(s -> s.getUser() != null && s.getUser().getId().equals(userId))
                .orElseThrow(() -> new ResourceNotFoundException("스타일을 찾을 수 없습니다."));

        badgeStyleRepository.delete(style);
    }
}
