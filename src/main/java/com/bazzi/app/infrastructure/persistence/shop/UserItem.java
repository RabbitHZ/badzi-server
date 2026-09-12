package com.bazzi.app.infrastructure.persistence.shop;

import com.bazzi.app.infrastructure.persistence.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Table(name = "user_item",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "shop_item_id"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_item_id", nullable = false)
    private ShopItem shopItem;

    @Column(name = "payment_id")
    private Long paymentId;

    @Column(name = "acquired_at", nullable = false)
    private OffsetDateTime acquiredAt;

    @Builder
    public UserItem(User user, ShopItem shopItem, Long paymentId) {
        this.user = user;
        this.shopItem = shopItem;
        this.paymentId = paymentId;
        this.acquiredAt = OffsetDateTime.now();
    }
}
