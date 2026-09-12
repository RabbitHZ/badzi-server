package com.bazzi.app.infrastructure.persistence.badge;

import com.bazzi.app.infrastructure.persistence.shop.ShopItem;
import com.bazzi.app.infrastructure.persistence.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Table(name = "badge_style")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BadgeStyle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "style_type", nullable = false, length = 50)
    private String styleType;

    @Column(length = 20)
    private String color;

    @Column(length = 100)
    private String label;

    @Column(length = 100)
    private String icon;

    @Column(name = "font_size")
    private Integer fontSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_item_id")
    private ShopItem shopItem;

    @Column(name = "is_preset", nullable = false)
    private boolean preset = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Builder
    public BadgeStyle(User user, String name, String styleType, String color,
                      String label, String icon, Integer fontSize,
                      ShopItem shopItem, boolean preset) {
        this.user = user;
        this.name = name;
        this.styleType = styleType;
        this.color = color;
        this.label = label;
        this.icon = icon;
        this.fontSize = fontSize;
        this.shopItem = shopItem;
        this.preset = preset;
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
    }

    public void update(String name, String color, String label, String icon, Integer fontSize) {
        this.name = name;
        this.color = color;
        this.label = label;
        this.icon = icon;
        this.fontSize = fontSize;
        this.updatedAt = OffsetDateTime.now();
    }
}
