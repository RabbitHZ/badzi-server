package com.bazzi.app.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
@Schema(description = "Badge 요청 DTO")
public class BadgeRequestDto {

    private static final String DEFAULT_STYLE = "basic";
    private static final String DEFAULT_COLOR = "#4CAF50";
    private static final String DEFAULT_LABEL = "Views";

    @Schema(description = "github url", example = "https://github.com/username")
    private String url = "";

    @Schema(description = "스타일 타입 (basic, maple, rabbit 또는 커스텀)", example = "basic")
    @Pattern(regexp = "^[a-zA-Z0-9_-]{1,50}$",
            message = "스타일 타입은 영문/숫자/_/- 1~50자")
    private String styleType = DEFAULT_STYLE;

    @Schema(description = "뱃지 색상 - basic 스타일에서만 적용 (색상명, hex, rgb 지원)", example = "#4CAF50")
    @Pattern(regexp = "^(#[0-9A-Fa-f]{6}|#[0-9A-Fa-f]{3}|[0-9A-Fa-f]{6}|[0-9A-Fa-f]{3}|rgb\\(\\s*\\d{1,3}\\s*,\\s*\\d{1,3}\\s*,\\s*\\d{1,3}\\s*\\)|[a-zA-Z]+)$",
            message = "색상값은 hex(#RRGGBB 또는 RRGGBB), rgb(r,g,b) 또는 색상명이어야 합니다")
    private String color = DEFAULT_COLOR;

    @Schema(description = "뱃지 라벨", example = "Views")
    private String label = DEFAULT_LABEL;

    public void setUrl(String url) {
        this.url = url == null ? "" : url;
    }

    public void setStyleType(String styleType) {
        this.styleType = (styleType == null || styleType.isBlank()) ? DEFAULT_STYLE : styleType;
    }

    public void setColor(String color) {
        this.color = (color == null || color.isBlank()) ? DEFAULT_COLOR : color;
    }

    public void setLabel(String label) {
        this.label = (label == null || label.isBlank()) ? DEFAULT_LABEL : label;
    }
}
