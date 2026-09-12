package com.bazzi.app.util.payment;

import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.UUID;

/**
 * 그로블 seller_reference 생성 유틸.
 * 규칙: A-Za-z0-9-_.:=~, ≤128자, 추측 불가(UUID 기반 base64url).
 * ref는 URL에 노출되므로 순번·PII 금지.
 */
@Component
public class SellerRefGenerator {

    public String generate() {
        byte[] uuidBytes = uuidToBytes(UUID.randomUUID());
        // base64url (no padding) = A-Za-z0-9-_ 문자만 사용
        return Base64.getUrlEncoder().withoutPadding().encodeToString(uuidBytes);
    }

    private byte[] uuidToBytes(UUID uuid) {
        byte[] bytes = new byte[16];
        long msb = uuid.getMostSignificantBits();
        long lsb = uuid.getLeastSignificantBits();
        for (int i = 0; i < 8; i++) {
            bytes[i] = (byte) (msb >>> (56 - 8 * i));
            bytes[i + 8] = (byte) (lsb >>> (56 - 8 * i));
        }
        return bytes;
    }
}
