package com.bazzi.app.application.service.shop.impl;

import com.bazzi.app.application.dto.response.shop.ShopItemResponse;
import com.bazzi.app.application.dto.response.shop.UserItemResponse;
import com.bazzi.app.application.exception.ResourceNotFoundException;
import com.bazzi.app.application.service.shop.ShopService;
import com.bazzi.app.infrastructure.persistence.shop.ShopItemRepository;
import com.bazzi.app.infrastructure.persistence.shop.UserItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {

    private final ShopItemRepository shopItemRepository;
    private final UserItemRepository userItemRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ShopItemResponse> getActiveItems() {
        return shopItemRepository.findAllByActiveTrue().stream()
                .map(ShopItemResponse::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ShopItemResponse getItem(Long itemId) {
        return shopItemRepository.findById(itemId)
                .map(ShopItemResponse::new)
                .orElseThrow(() -> new ResourceNotFoundException("상품을 찾을 수 없습니다."));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserItemResponse> getUserItems(Long userId) {
        return userItemRepository.findAllByUserId(userId).stream()
                .map(UserItemResponse::new)
                .toList();
    }

    @Override
    public boolean userOwnsItem(Long userId, Long shopItemId) {
        return userItemRepository.existsByUserIdAndShopItemId(userId, shopItemId);
    }
}
