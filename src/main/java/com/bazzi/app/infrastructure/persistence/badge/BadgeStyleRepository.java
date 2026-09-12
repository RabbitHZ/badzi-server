package com.bazzi.app.infrastructure.persistence.badge;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BadgeStyleRepository extends JpaRepository<BadgeStyle, Long> {
    List<BadgeStyle> findAllByUserId(Long userId);
    List<BadgeStyle> findAllByPresetTrue();
    Optional<BadgeStyle> findByShopItemId(Long shopItemId);
    Optional<BadgeStyle> findByStyleTypeAndPresetTrue(String styleType);
}
