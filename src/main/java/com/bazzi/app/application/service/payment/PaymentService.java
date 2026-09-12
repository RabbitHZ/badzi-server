package com.bazzi.app.application.service.payment;

import com.bazzi.app.application.dto.request.payment.CreatePaymentRequest;
import com.bazzi.app.application.dto.response.payment.CreatePaymentResponse;
import com.bazzi.app.application.dto.response.payment.PaymentResponse;

import java.util.List;
import java.util.Map;

public interface PaymentService {
    CreatePaymentResponse createPayment(Long userId, CreatePaymentRequest request);
    void handleWebhook(Map<String, Object> payload);
    List<PaymentResponse> getUserPayments(Long userId);
    PaymentResponse getPayment(Long userId, Long paymentId);
}
