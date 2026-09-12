package com.bazzi.app.interfaces.controller.badge;

import com.bazzi.app.application.dto.request.badge.BadgeStyleRequest;
import com.bazzi.app.application.dto.response.badge.BadgeStyleResponse;
import com.bazzi.app.application.service.badge.BadgeStyleService;
import com.bazzi.app.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "BadgeStyles", description = "뱃지 커스텀 스타일 API")
@RestController
@RequestMapping("/api/badge-styles")
@RequiredArgsConstructor
public class BadgeStyleController {

    private final BadgeStyleService badgeStyleService;

    @Operation(summary = "내 스타일 목록 조회", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping
    public ResponseEntity<ApiResponse<List<BadgeStyleResponse>>> getMyStyles(
            @AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(ApiResponse.success("스타일 목록 조회 성공",
                badgeStyleService.getUserStyles(userId)));
    }

    @Operation(summary = "스타일 생성", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping
    public ResponseEntity<ApiResponse<BadgeStyleResponse>> createStyle(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody BadgeStyleRequest request) {
        return ResponseEntity.ok(ApiResponse.success("스타일 생성 성공",
                badgeStyleService.createStyle(userId, request)));
    }

    @Operation(summary = "스타일 수정", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BadgeStyleResponse>> updateStyle(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long id,
            @Valid @RequestBody BadgeStyleRequest request) {
        return ResponseEntity.ok(ApiResponse.success("스타일 수정 성공",
                badgeStyleService.updateStyle(userId, id, request)));
    }

    @Operation(summary = "스타일 삭제", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStyle(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long id) {
        badgeStyleService.deleteStyle(userId, id);
        return ResponseEntity.ok(ApiResponse.success("스타일 삭제 성공", null));
    }
}
