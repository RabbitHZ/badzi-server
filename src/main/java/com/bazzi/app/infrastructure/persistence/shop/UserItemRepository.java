package com.bazzi.app.infrastructure.persistence.shop;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserItemRepository extends JpaRepository<UserItem, Long> {
    List<UserItem> findAllByUserId(Long userId);
    Optional<UserItem> findByUserIdAndShopItemId(Long userId, Long shopItemId);
    boolean existsByUserIdAndShopItemId(Long userId, Long shopItemId);
}
