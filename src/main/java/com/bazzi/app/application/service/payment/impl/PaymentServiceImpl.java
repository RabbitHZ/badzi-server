package com.bazzi.app.application.service.payment.impl;

import com.bazzi.app.application.dto.request.payment.CreatePaymentRequest;
import com.bazzi.app.application.dto.response.payment.CreatePaymentResponse;
import com.bazzi.app.application.dto.response.payment.PaymentResponse;
import com.bazzi.app.application.exception.ResourceNotFoundException;
import com.bazzi.app.application.gateway.PaymentGateway;
import com.bazzi.app.application.service.payment.PaymentService;
import com.bazzi.app.infrastructure.persistence.payment.Payment;
import com.bazzi.app.infrastructure.persistence.payment.PaymentRepository;
import com.bazzi.app.infrastructure.persistence.payment.PaymentStatus;
import com.bazzi.app.infrastructure.persistence.shop.ShopItem;
import com.bazzi.app.infrastructure.persistence.shop.ShopItemRepository;
import com.bazzi.app.infrastructure.persistence.shop.UserItem;
import com.bazzi.app.infrastructure.persistence.shop.UserItemRepository;
import com.bazzi.app.infrastructure.persistence.user.User;
import com.bazzi.app.infrastructure.persistence.user.UserRepository;
import com.bazzi.app.util.payment.SellerRefGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final ShopItemRepository shopItemRepository;
    private final UserItemRepository userItemRepository;
    private final UserRepository userRepository;
    private final PaymentGateway paymentGateway;
    private final SellerRefGenerator sellerRefGenerator;

    @Override
    @Transactional
    public CreatePaymentResponse createPayment(Long userId, CreatePaymentRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));
        ShopItem shopItem = shopItemRepository.findById(request.getShopItemId())
                .orElseThrow(() -> new ResourceNotFoundException("상품을 찾을 수 없습니다."));

        String orderNo = UUID.randomUUID().toString().replace("-", "").substring(0, 32);
        String sellerReference = sellerRefGenerator.generate();

        Payment payment = Payment.builder()
                .user(user)
                .shopItem(shopItem)
                .orderNo(orderNo)
                .sellerReference(sellerReference)
                .amountKrw(shopItem.getPriceKrw())
                .build();
        payment = paymentRepository.save(payment);

        String paymentUrl = paymentGateway.buildPaymentUrl(
                shopItem.getGrobleProductId(), sellerReference);

        return new CreatePaymentResponse(payment.getId(), orderNo, paymentUrl);
    }

    @Override
    @Transactional
    public void handleWebhook(Map<String, Object> payload) {
        String sellerReference = extractSellerReference(payload);
        if (sellerReference == null) return;

        Payment payment = paymentRepository.findBySellerReference(sellerReference)
                .orElse(null);
        if (payment == null) return;

        // 이미 처리된 웹훅 → 멱등 처리
        if (payment.getStatus() == PaymentStatus.PAID) return;

        String eventType = extractEventType(payload);

        if ("payment.completed".equals(eventType) || "completed".equals(eventType)) {
            String pgTxId = extractPgTxId(payload);
            payment.markPaid(pgTxId);
            grantItem(payment);
        } else if ("payment.failed".equals(eventType) || "failed".equals(eventType)) {
            payment.markFailed();
        } else if ("payment.cancelled".equals(eventType) || "cancelled".equals(eventType)) {
            payment.markCancelled();
        }
    }

    private void grantItem(Payment payment) {
        Long userId = payment.getUser().getId();
        Long shopItemId = payment.getShopItem().getId();

        if (userItemRepository.existsByUserIdAndShopItemId(userId, shopItemId)) return;

        UserItem userItem = UserItem.builder()
                .user(payment.getUser())
                .shopItem(payment.getShopItem())
                .paymentId(payment.getId())
                .build();
        userItemRepository.save(userItem);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> getUserPayments(Long userId) {
        return paymentRepository.findAllByUserId(userId).stream()
                .map(PaymentResponse::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPayment(Long userId, Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .filter(p -> p.getUser().getId().equals(userId))
                .orElseThrow(() -> new ResourceNotFoundException("결제 내역을 찾을 수 없습니다."));
        return new PaymentResponse(payment);
    }

    @SuppressWarnings("unchecked")
    private String extractSellerReference(Map<String, Object> payload) {
        try {
            Map<String, Object> data = (Map<String, Object>) payload.get("data");
            if (data == null) return null;
            Map<String, Object> object = (Map<String, Object>) data.get("object");
            if (object == null) return null;
            return (String) object.get("sellerReference");
        } catch (Exception e) {
            return null;
        }
    }

    private String extractEventType(Map<String, Object> payload) {
        Object type = payload.get("type");
        return type != null ? type.toString() : null;
    }

    @SuppressWarnings("unchecked")
    private String extractPgTxId(Map<String, Object> payload) {
        try {
            Map<String, Object> data = (Map<String, Object>) payload.get("data");
            if (data == null) return null;
            Map<String, Object> object = (Map<String, Object>) data.get("object");
            if (object == null) return null;
            Object id = object.get("id");
            return id != null ? id.toString() : null;
        } catch (Exception e) {
            return null;
        }
    }
}
