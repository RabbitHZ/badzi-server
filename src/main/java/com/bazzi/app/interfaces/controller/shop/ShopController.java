package com.bazzi.app.interfaces.controller.shop;

import com.bazzi.app.application.dto.response.shop.ShopItemResponse;
import com.bazzi.app.application.service.shop.ShopService;
import com.bazzi.app.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Shop", description = "상점 API")
@RestController
@RequestMapping("/api/shop")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;

    @Operation(summary = "상품 목록 조회")
    @GetMapping("/items")
    public ResponseEntity<ApiResponse<List<ShopItemResponse>>> getItems() {
        return ResponseEntity.ok(ApiResponse.success("상품 목록 조회 성공", shopService.getActiveItems()));
    }

    @Operation(summary = "상품 단건 조회")
    @GetMapping("/items/{id}")
    public ResponseEntity<ApiResponse<ShopItemResponse>> getItem(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("상품 조회 성공", shopService.getItem(id)));
    }
}
