package com.bazzi.app.application.service.badge;

import com.bazzi.app.application.dto.request.badge.BadgeStyleRequest;
import com.bazzi.app.application.dto.response.badge.BadgeStyleResponse;

import java.util.List;

public interface BadgeStyleService {
    List<BadgeStyleResponse> getUserStyles(Long userId);
    BadgeStyleResponse createStyle(Long userId, BadgeStyleRequest request);
    BadgeStyleResponse updateStyle(Long userId, Long styleId, BadgeStyleRequest request);
    void deleteStyle(Long userId, Long styleId);
}
