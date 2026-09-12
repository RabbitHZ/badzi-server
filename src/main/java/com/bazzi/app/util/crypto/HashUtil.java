package com.bazzi.app.util.crypto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;

/**
 * SHA-256 blind index 해시 유틸.
 * 이메일 같은 필드를 조회 가능하게 고정 길이 해시로 저장한다.
 */
@Component
public class HashUtil {

    private final String salt;

    public HashUtil(@Value("${app.encryption.salt}") String salt) {
        this.salt = salt;
    }

    public String hash(String plaintext) {
        if (plaintext == null) return null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] hashBytes = digest.digest(plaintext.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashBytes);
        } catch (Exception e) {
            throw new IllegalStateException("해시 생성 실패", e);
        }
    }
}
