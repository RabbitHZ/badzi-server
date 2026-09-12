package com.bazzi.app.infrastructure.persistence.user;

import com.bazzi.app.util.crypto.AesEncryptor;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String provider;

    @Column(name = "provider_id", nullable = false, length = 100)
    private String providerId;

    @Column(length = 100)
    private String username;

    @Convert(converter = AesEncryptor.class)
    @Column(columnDefinition = "TEXT")
    private String email;

    @Column(name = "email_hash", nullable = false, length = 64, unique = true)
    private String emailHash;

    @Convert(converter = AesEncryptor.class)
    @Column(columnDefinition = "TEXT")
    private String name;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Builder
    public User(String provider, String providerId, String username,
                String email, String emailHash, String name) {
        this.provider = provider;
        this.providerId = providerId;
        this.username = username;
        this.email = email;
        this.emailHash = emailHash;
        this.name = name;
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
    }

    public void updateProfile(String email, String emailHash, String name, String username) {
        this.email = email;
        this.emailHash = emailHash;
        this.name = name;
        if (username != null) this.username = username;
        this.updatedAt = OffsetDateTime.now();
    }
}
