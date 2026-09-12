package com.bazzi.app.application.dto.response.payment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreatePaymentResponse {

    @Schema(description = "결제 ID")
    private final Long paymentId;

    @Schema(description = "주문번호 (멱등키)")
    private final String orderNo;

    @Schema(description = "그로블 결제 URL (?ref= 포함)")
    private final String paymentUrl;
}
