package com.bazzi.app.infrastructure.persistence.shop;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShopItemRepository extends JpaRepository<ShopItem, Long> {
    List<ShopItem> findAllByActiveTrue();
}
