package com.bazzi.app.application.dto.request.badge;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BadgeStyleRequest {

    @NotBlank
    @Schema(description = "스타일 이름")
    private String name;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9_-]{1,50}$", message = "styleType은 영문/숫자/_/- 1~50자")
    @Schema(description = "스타일 타입 (예: basic, maple, custom)")
    private String styleType;

    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "color는 #RRGGBB 형식")
    @Schema(description = "색상 (#RRGGBB)")
    private String color;

    @Schema(description = "라벨 텍스트")
    private String label;

    @Schema(description = "아이콘")
    private String icon;

    @Schema(description = "폰트 크기")
    private Integer fontSize;
}
