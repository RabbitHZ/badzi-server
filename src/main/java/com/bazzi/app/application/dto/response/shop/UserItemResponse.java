package com.bazzi.app.application.dto.response.shop;

import com.bazzi.app.infrastructure.persistence.shop.UserItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
public class UserItemResponse {

    @Schema(description = "보유 레코드 ID")
    private final Long id;

    @Schema(description = "상품 정보")
    private final ShopItemResponse shopItem;

    @Schema(description = "획득 일시")
    private final OffsetDateTime acquiredAt;

    public UserItemResponse(UserItem userItem) {
        this.id = userItem.getId();
        this.shopItem = new ShopItemResponse(userItem.getShopItem());
        this.acquiredAt = userItem.getAcquiredAt();
    }
}
