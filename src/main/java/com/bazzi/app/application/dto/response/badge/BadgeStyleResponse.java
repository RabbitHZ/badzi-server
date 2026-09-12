package com.bazzi.app.application.dto.response.badge;

import com.bazzi.app.infrastructure.persistence.badge.BadgeStyle;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class BadgeStyleResponse {

    @Schema(description = "스타일 ID")
    private final Long id;

    @Schema(description = "스타일 이름")
    private final String name;

    @Schema(description = "스타일 타입")
    private final String styleType;

    @Schema(description = "색상")
    private final String color;

    @Schema(description = "라벨")
    private final String label;

    @Schema(description = "아이콘")
    private final String icon;

    @Schema(description = "폰트 크기")
    private final Integer fontSize;

    @Schema(description = "프리셋 여부")
    private final boolean preset;

    @Schema(description = "연결된 상품 ID")
    private final Long shopItemId;

    public BadgeStyleResponse(BadgeStyle style) {
        this.id = style.getId();
        this.name = style.getName();
        this.styleType = style.getStyleType();
        this.color = style.getColor();
        this.label = style.getLabel();
        this.icon = style.getIcon();
        this.fontSize = style.getFontSize();
        this.preset = style.isPreset();
        this.shopItemId = style.getShopItem() != null ? style.getShopItem().getId() : null;
    }
}
