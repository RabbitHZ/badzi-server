package com.bazzi.app.interfaces.controller;

import com.bazzi.app.application.dto.request.BadgeRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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

    @Operation(summary = "뱃지 생성", description = "조회수를 포함한 실시간 뱃지를 생성하고 조회수를 1 증가")
    @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> generateBadge(@ModelAttribute BadgeRequestDto request){
        String svg = "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"150\" height=\"20\">" +
                "<rect width=\"150\" height=\"20\" fill=\"#555\" />" +
                "<text x=\"10\" y=\"14\" style=\"fill: #00FF00; font-size: 14px;\">Views: " + 123 + "/" + 123 + "</text>" +
                "</svg>";
        return ResponseEntity.ok(svg);
    }

    @Operation(summary = "뱃지 미리보기 생성", description = "조회수를 증가시키지 않고 뱃지를 미리 조회.")
    @GetMapping(value = "/preview", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> generatePreviewBadge(@ModelAttribute BadgeRequestDto request){
        String svg = "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"150\" height=\"20\">" +
                "<rect width=\"150\" height=\"20\" fill=\"#555\" />" +
                "<text x=\"10\" y=\"14\" style=\"fill: #00FF00; font-size: 14px;\">Views: " + 0 + "/" + 0 + "</text>" +
                "</svg>";
        return ResponseEntity.ok(svg);
    }
}
