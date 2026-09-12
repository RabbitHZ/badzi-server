package com.bazzi.app.application.service.shop;

import com.bazzi.app.application.dto.response.shop.ShopItemResponse;
import com.bazzi.app.application.dto.response.shop.UserItemResponse;

import java.util.List;

public interface ShopService {
    List<ShopItemResponse> getActiveItems();
    ShopItemResponse getItem(Long itemId);
    List<UserItemResponse> getUserItems(Long userId);
    boolean userOwnsItem(Long userId, Long shopItemId);
}
