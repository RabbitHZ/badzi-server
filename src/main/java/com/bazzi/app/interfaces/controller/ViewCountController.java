package com.bazzi.app.interfaces.controller;

import com.bazzi.app.application.dto.response.ViewCountResponseDto;
import com.bazzi.app.common.response.ApiResponse;
import com.bazzi.app.common.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "View count API", description = "조회수 관리 API")
@RestController
@RequestMapping("/api/viewcounts")
@RequiredArgsConstructor
public class ViewCountController {

    @Operation(summary = "조회수 조회", description = "특정 사용자의 오늘과 전체 조회수를 조회")
    @GetMapping("/{username}")
    public ApiResponse<ViewCountResponseDto> getViewCount(
            @Parameter(name = "username", description = "사용자명", example = "username") @PathVariable("username") String username) {
        return ApiResponse.success(ResponseCode.VIEW_COUNT_READ_SUCCESS.getMessage(), new ViewCountResponseDto(0,0));
    }

    @Operation(summary = "조회수 증가", description = "특정 사용자의 오늘과 전체 조회수를 1 증가")
    @PostMapping("/{username}")
    public ApiResponse<ViewCountResponseDto> increaseViewCount(
            @Parameter(name = "username", description = "사용자명", example = "username") @PathVariable("username") String username) {
        return ApiResponse.success(ResponseCode.VIEW_COUNT_INCREASE_SUCCESS.getMessage(), new ViewCountResponseDto(1,1));
    }

    @Operation(summary = "조회수 초기화", description = "특정 사용자의 오늘과 전체 조회수를 0으로 초기화")
    @PatchMapping("/{username}")
    public ApiResponse<ViewCountResponseDto> resetViewCount(
            @Parameter(name = "username", description = "사용자명", example = "username") @PathVariable("username") String username) {
        return ApiResponse.success(ResponseCode.VIEW_COUNT_RESET_SUCCESS.getMessage(), new ViewCountResponseDto(0,0));
    }
}
