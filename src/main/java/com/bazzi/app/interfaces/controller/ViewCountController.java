package com.bazzi.app.interfaces.controller;

import com.bazzi.app.application.dto.response.ViewCountResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "View count API", description = "조회수 관리 API")
@RestController
@RequestMapping("/api/viewcount")
@RequiredArgsConstructor
public class ViewCountController {

    @Operation(summary = "조회수 조회", description = "특정 사용자의 오늘과 전체 조회수를 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회수 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ViewCountResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 - username이 누락됨", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content)})
    @GetMapping("/{username}")
    public ResponseEntity<ViewCountResponseDto> getViewCount(
            @Parameter(name = "username", description = "사용자명", example = "username") @PathVariable("username") String username) {
        return ResponseEntity.ok(new ViewCountResponseDto(0,0));
    }

    @Operation(summary = "조회수 증가", description = "특정 사용자의 오늘과 전체 조회수를 1 증가")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회수 증가 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ViewCountResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 - username이 누락됨", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content)
    })
    @GetMapping("/{username}/increment")
    public ResponseEntity<ViewCountResponseDto> increaseViewCount(
            @Parameter(name = "username", description = "사용자명", example = "username") @PathVariable("username") String username) {
        return ResponseEntity.ok(new ViewCountResponseDto(1,1));
    }

    @Operation(summary = "조회수 초기화", description = "특정 사용자의 오늘과 전체 조회수를 0으로 초기화")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회수 초기화 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 - username이 누락됨", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content)
    })
    @PostMapping("/{username}/reset")
    public ResponseEntity<Void> resetViewCount(
            @Parameter(name = "username", description = "사용자명", example = "username") @PathVariable("username") String username) {
        return ResponseEntity.ok().build();
    }
}
