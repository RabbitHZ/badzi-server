package com.bazzi.app.payment;

import com.bazzi.app.application.gateway.PaymentGateway;
import com.bazzi.app.application.service.payment.impl.PaymentServiceImpl;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock private PaymentRepository paymentRepository;
    @Mock private ShopItemRepository shopItemRepository;
    @Mock private UserItemRepository userItemRepository;
    @Mock private UserRepository userRepository;
    @Mock private PaymentGateway paymentGateway;
    @Mock private SellerRefGenerator sellerRefGenerator;

    private PaymentServiceImpl paymentService;

    @BeforeEach
    void setUp() {
        paymentService = new PaymentServiceImpl(
                paymentRepository, shopItemRepository, userItemRepository,
                userRepository, paymentGateway, sellerRefGenerator);
    }

    private Map<String, Object> buildWebhookPayload(String type, String sellerRef, String pgTxId) {
        return Map.of(
                "type", type,
                "data", Map.of(
                        "object", Map.of(
                                "sellerReference", sellerRef,
                                "id", pgTxId
                        )
                )
        );
    }

    private Payment mockPayment(PaymentStatus status) {
        User user = mock(User.class);
        lenient().when(user.getId()).thenReturn(1L);
        ShopItem shopItem = mock(ShopItem.class);
        lenient().when(shopItem.getId()).thenReturn(10L);

        Payment payment = mock(Payment.class);
        when(payment.getStatus()).thenReturn(status);
        lenient().when(payment.getUser()).thenReturn(user);
        lenient().when(payment.getShopItem()).thenReturn(shopItem);
        lenient().when(payment.getId()).thenReturn(100L);
        return payment;
    }

    @Test
    void 웹훅_성공_PAID로_전이되고_user_item_생성() {
        Payment payment = mockPayment(PaymentStatus.PENDING);
        when(paymentRepository.findBySellerReference("ref123")).thenReturn(Optional.of(payment));
        when(userItemRepository.existsByUserIdAndShopItemId(1L, 10L)).thenReturn(false);

        paymentService.handleWebhook(buildWebhookPayload("payment.completed", "ref123", "pg-tx-001"));

        verify(payment).markPaid("pg-tx-001");
        verify(userItemRepository).save(any(UserItem.class));
    }

    @Test
    void 웹훅_중복_PAID_상태면_user_item_재생성_안함() {
        Payment payment = mockPayment(PaymentStatus.PAID);
        when(paymentRepository.findBySellerReference("ref123")).thenReturn(Optional.of(payment));

        paymentService.handleWebhook(buildWebhookPayload("payment.completed", "ref123", "pg-tx-001"));

        verify(payment, never()).markPaid(any());
        verify(userItemRepository, never()).save(any());
    }

    @Test
    void 웹훅_실패_FAILED로_전이() {
        Payment payment = mockPayment(PaymentStatus.PENDING);
        when(paymentRepository.findBySellerReference("ref456")).thenReturn(Optional.of(payment));

        paymentService.handleWebhook(buildWebhookPayload("payment.failed", "ref456", "pg-tx-002"));

        verify(payment).markFailed();
        verify(userItemRepository, never()).save(any());
    }

    @Test
    void 웹훅_취소_CANCELLED로_전이() {
        Payment payment = mockPayment(PaymentStatus.PENDING);
        when(paymentRepository.findBySellerReference("ref789")).thenReturn(Optional.of(payment));

        paymentService.handleWebhook(buildWebhookPayload("payment.cancelled", "ref789", "pg-tx-003"));

        verify(payment).markCancelled();
        verify(userItemRepository, never()).save(any());
    }

    @Test
    void 알수없는_ref_웹훅은_무시() {
        when(paymentRepository.findBySellerReference(anyString())).thenReturn(Optional.empty());

        paymentService.handleWebhook(buildWebhookPayload("payment.completed", "unknown-ref", "tx"));

        verify(userItemRepository, never()).save(any());
    }

    @Test
    void user_item_이미_있으면_중복_생성_안함() {
        Payment payment = mockPayment(PaymentStatus.PENDING);
        when(paymentRepository.findBySellerReference("ref_dup")).thenReturn(Optional.of(payment));
        when(userItemRepository.existsByUserIdAndShopItemId(1L, 10L)).thenReturn(true);

        paymentService.handleWebhook(buildWebhookPayload("payment.completed", "ref_dup", "tx"));

        verify(payment).markPaid("tx");
        verify(userItemRepository, never()).save(any());
    }
}
