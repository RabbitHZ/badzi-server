package com.bazzi.app.application.dto.response.shop;

import com.bazzi.app.infrastructure.persistence.shop.ShopItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ShopItemResponse {

    @Schema(description = "상품 ID")
    private final Long id;

    @Schema(description = "상품명")
    private final String name;

    @Schema(description = "상품 설명")
    private final String description;

    @Schema(description = "가격 (원)")
    private final int priceKrw;

    public ShopItemResponse(ShopItem item) {
        this.id = item.getId();
        this.name = item.getName();
        this.description = item.getDescription();
        this.priceKrw = item.getPriceKrw();
    }
}
