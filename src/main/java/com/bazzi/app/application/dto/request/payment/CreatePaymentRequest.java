package com.bazzi.app.application.dto.request.payment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreatePaymentRequest {

    @NotNull
    @Schema(description = "구매할 상품 ID")
    private Long shopItemId;
}
