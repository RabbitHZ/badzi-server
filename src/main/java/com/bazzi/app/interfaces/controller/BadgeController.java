package com.bazzi.app.interfaces.controller;

import com.bazzi.app.application.dto.request.BadgeRequestDto;
import com.bazzi.app.application.dto.response.ViewCountResponseDto;
import com.bazzi.app.application.service.ViewCountService;
import com.bazzi.app.util.SvgGenerator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Badge API", description = "뱃지 관리 API")
@RestController
@RequestMapping("/api/badges")
@RequiredArgsConstructor
public class BadgeController {

    private final ViewCountService viewCountService;

    @Operation(summary = "뱃지 생성", description = "조회수를 포함한 실시간 뱃지를 생성하고 조회수를 1 증가")
    @GetMapping(produces = "image/svg+xml")
    public ResponseEntity<String> generateBadge(@ModelAttribute BadgeRequestDto request){
        String url = request.getUrl();
        String username = url.substring(url.lastIndexOf("/") + 1);
        ViewCountResponseDto responseDto = viewCountService.incrementViewCount(username);
        String svg = SvgGenerator.generateBadge(request.getColor(), request.getLabel(),request.getFontSize(), responseDto.getToday(), responseDto.getTotal());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("image/svg+xml"));
        headers.setCacheControl("private, no-cache, no-store, must-revalidate, max-age=0");

        return ResponseEntity.ok()
                .headers(headers)
                .body(svg);
    }

    @Operation(summary = "뱃지 미리보기 생성", description = "조회수를 증가시키지 않고 뱃지를 미리 조회.")
    @GetMapping(value = "/preview", produces = "image/svg+xml")
    public ResponseEntity<String> generatePreviewBadge(@ModelAttribute BadgeRequestDto request){
        String url = request.getUrl();
        String username = url.substring(url.lastIndexOf("/") + 1);
        String svg = SvgGenerator.generateBadge(request.getColor(), request.getLabel(), request.getFontSize(), 0, 0);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("image/svg+xml"));

        return ResponseEntity.ok()
                .headers(headers)
                .body(svg);
    }
}
