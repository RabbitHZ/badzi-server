package com.bazzi.app.interfaces.controller.payment;

import com.bazzi.app.application.dto.request.payment.CreatePaymentRequest;
import com.bazzi.app.application.dto.response.payment.CreatePaymentResponse;
import com.bazzi.app.application.dto.response.payment.PaymentResponse;
import com.bazzi.app.application.service.payment.PaymentService;
import com.bazzi.app.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Payments", description = "결제 API")
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "결제 요청 생성", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping
    public ResponseEntity<ApiResponse<CreatePaymentResponse>> createPayment(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody CreatePaymentRequest request) {
        CreatePaymentResponse response = paymentService.createPayment(userId, request);
        return ResponseEntity.ok(ApiResponse.success("결제 URL이 생성되었습니다.", response));
    }

    @Operation(summary = "그로블 결제 완료 웹훅 수신")
    @PostMapping("/webhook")
    public ResponseEntity<Void> webhook(@RequestBody Map<String, Object> payload) {
        paymentService.handleWebhook(payload);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "내 결제 목록 조회", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getMyPayments(
            @AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(ApiResponse.success("결제 목록 조회 성공",
                paymentService.getUserPayments(userId)));
    }

    @Operation(summary = "결제 상세 조회", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPayment(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("결제 조회 성공",
                paymentService.getPayment(userId, id)));
    }
}
