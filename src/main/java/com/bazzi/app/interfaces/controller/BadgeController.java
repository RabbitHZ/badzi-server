package com.bazzi.app.interfaces.controller;

import com.bazzi.app.application.dto.request.BadgeRequestDto;
import com.bazzi.app.application.dto.response.ViewCountResponseDto;
import com.bazzi.app.application.exception.AccessDeniedException;
import com.bazzi.app.application.service.ViewCountService;
import com.bazzi.app.application.service.shop.ShopService;
import com.bazzi.app.infrastructure.persistence.badge.BadgeStyle;
import com.bazzi.app.infrastructure.persistence.badge.BadgeStyleRepository;
import com.bazzi.app.util.badge.BadgeGeneratorFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@Tag(name = "Badge API", description = "뱃지 관리 API")
@RestController
@RequestMapping("/api/badges")
@RequiredArgsConstructor
public class BadgeController {

    private static final Set<String> FREE_STYLE_TYPES = Set.of("basic", "maple", "rabbit", "dream_rabbit", "cat");

    private final ViewCountService viewCountService;
    private final BadgeGeneratorFactory badgeGeneratorFactory;
    private final ShopService shopService;
    private final BadgeStyleRepository badgeStyleRepository;

    @Operation(summary = "뱃지 생성", description = "조회수를 포함한 실시간 뱃지를 생성하고 조회수를 1 증가")
    @GetMapping(produces = "image/svg+xml")
    public ResponseEntity<String> generateBadge(@ModelAttribute BadgeRequestDto request){
        checkStyleAccess(request.getStyleType());

        String url = request.getUrl();
        String username = url.substring(url.lastIndexOf("/") + 1);
        ViewCountResponseDto responseDto = viewCountService.incrementViewCount(username);

        String svg = badgeGeneratorFactory.generateBadge(
                request.getStyleType(),
                request.getColor(),
                request.getLabel(),
                responseDto.getToday(),
                responseDto.getTotal()
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("image/svg+xml"));
        headers.setCacheControl("private");
        headers.remove("ETag");

        return ResponseEntity.ok()
                .headers(headers)
                .body(svg);
    }

    @Operation(summary = "뱃지 미리보기 생성", description = "조회수를 증가시키지 않고 뱃지를 미리 조회.")
    @GetMapping(value = "/preview", produces = "image/svg+xml")
    public ResponseEntity<String> generatePreviewBadge(@ModelAttribute BadgeRequestDto request){
        String svg = badgeGeneratorFactory.generateBadge(
                request.getStyleType(),
                request.getColor(),
                request.getLabel(),
                0,
                0
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("image/svg+xml"));

        return ResponseEntity.ok()
                .headers(headers)
                .body(svg);
    }

    private void checkStyleAccess(String styleType) {
        if (FREE_STYLE_TYPES.contains(styleType)) return;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || !(auth.getPrincipal() instanceof Long userId)) {
            throw new AccessDeniedException("유료 스타일은 로그인이 필요합니다.");
        }

        BadgeStyle preset = badgeStyleRepository
                .findByStyleTypeAndPresetTrue(styleType)
                .orElse(null);

        if (preset != null && preset.getShopItem() != null) {
            Long shopItemId = preset.getShopItem().getId();
            if (!shopService.userOwnsItem(userId, shopItemId)) {
                throw new AccessDeniedException("해당 스타일을 보유하고 있지 않습니다.");
            }
        }
    }
}
