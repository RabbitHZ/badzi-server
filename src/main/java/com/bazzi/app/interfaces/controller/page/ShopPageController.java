package com.bazzi.app.interfaces.controller.page;

import com.bazzi.app.application.dto.response.shop.ShopItemResponse;
import com.bazzi.app.application.service.shop.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shop")
@RequiredArgsConstructor
public class ShopPageController {

    private final ShopService shopService;

    @GetMapping(value = "/items/{id}", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> itemPage(@PathVariable Long id) {
        ShopItemResponse item = shopService.getItem(id);
        String html = buildItemPage(item);
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(html);
    }

    private String buildItemPage(ShopItemResponse item) {
        return """
                <!DOCTYPE html>
                <html lang="ko">
                <head>
                  <meta charset="UTF-8">
                  <meta name="viewport" content="width=device-width, initial-scale=1.0">
                  <title>%s - BADZI 상점</title>
                  <style>
                    body { font-family: sans-serif; max-width: 600px; margin: 40px auto; padding: 0 20px; }
                    h1 { font-size: 1.5rem; }
                    .price { font-size: 1.2rem; font-weight: bold; color: #333; }
                    .desc { color: #666; margin: 16px 0; }
                    .btn { display: inline-block; padding: 12px 24px; background: #222; color: #fff;
                           border-radius: 6px; text-decoration: none; cursor: pointer; border: none;
                           font-size: 1rem; }
                  </style>
                </head>
                <body>
                  <h1>%s</h1>
                  <p class="desc">%s</p>
                  <p class="price">%,d원</p>
                  <p id="payment-area">
                    <!-- 그로블 결제 버튼은 스펙 확보 후 구현 예정 -->
                    <button class="btn" disabled>결제하기 (준비 중)</button>
                  </p>
                  <p><a href="/">홈으로 돌아가기</a></p>
                </body>
                </html>
                """.formatted(
                escapeHtml(item.getName()),
                escapeHtml(item.getName()),
                item.getDescription() != null ? escapeHtml(item.getDescription()) : "",
                item.getPriceKrw()
        );
    }

    private String escapeHtml(String text) {
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}
