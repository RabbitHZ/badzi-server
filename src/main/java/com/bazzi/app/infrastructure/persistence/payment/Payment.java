package com.bazzi.app.infrastructure.persistence.payment;

import com.bazzi.app.infrastructure.persistence.shop.ShopItem;
import com.bazzi.app.infrastructure.persistence.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Table(name = "payment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_item_id", nullable = false)
    private ShopItem shopItem;

    @Column(name = "order_no", nullable = false, unique = true, length = 64)
    private String orderNo;

    @Column(name = "seller_reference", nullable = false, unique = true, length = 128)
    private String sellerReference;

    @Column(name = "amount_krw", nullable = false)
    private int amountKrw;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus status;

    @Column(name = "pg_tx_id", length = 200)
    private String pgTxId;

    @Column(name = "paid_at")
    private OffsetDateTime paidAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Builder
    public Payment(User user, ShopItem shopItem, String orderNo,
                   String sellerReference, int amountKrw) {
        this.user = user;
        this.shopItem = shopItem;
        this.orderNo = orderNo;
        this.sellerReference = sellerReference;
        this.amountKrw = amountKrw;
        this.status = PaymentStatus.PENDING;
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
    }

    public void markPaid(String pgTxId) {
        this.status = PaymentStatus.PAID;
        this.pgTxId = pgTxId;
        this.paidAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
    }

    public void markFailed() {
        this.status = PaymentStatus.FAILED;
        this.updatedAt = OffsetDateTime.now();
    }

    public void markCancelled() {
        this.status = PaymentStatus.CANCELLED;
        this.updatedAt = OffsetDateTime.now();
    }
}
