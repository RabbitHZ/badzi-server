package com.bazzi.app.application.dto.response.payment;

import com.bazzi.app.infrastructure.persistence.payment.Payment;
import com.bazzi.app.infrastructure.persistence.payment.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
public class PaymentResponse {

    @Schema(description = "결제 ID")
    private final Long id;

    @Schema(description = "상품 ID")
    private final Long shopItemId;

    @Schema(description = "주문번호")
    private final String orderNo;

    @Schema(description = "결제 금액 (원)")
    private final int amountKrw;

    @Schema(description = "결제 상태")
    private final PaymentStatus status;

    @Schema(description = "결제 완료 일시")
    private final OffsetDateTime paidAt;

    @Schema(description = "생성 일시")
    private final OffsetDateTime createdAt;

    public PaymentResponse(Payment payment) {
        this.id = payment.getId();
        this.shopItemId = payment.getShopItem().getId();
        this.orderNo = payment.getOrderNo();
        this.amountKrw = payment.getAmountKrw();
        this.status = payment.getStatus();
        this.paidAt = payment.getPaidAt();
        this.createdAt = payment.getCreatedAt();
    }
}
