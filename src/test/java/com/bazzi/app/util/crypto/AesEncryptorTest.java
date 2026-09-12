package com.bazzi.app.util.crypto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

class AesEncryptorTest {

    private AesEncryptor encryptor;

    @BeforeEach
    void setUp() {
        // 테스트용 32바이트 AES 키 (base64 인코딩)
        String testKey = Base64.getEncoder().encodeToString(new byte[32]);
        encryptor = new AesEncryptor(testKey);
    }

    @Test
    void 암호화_후_복호화하면_원문과_같다() {
        String original = "test@example.com";
        String encrypted = encryptor.convertToDatabaseColumn(original);
        String decrypted = encryptor.convertToEntityAttribute(encrypted);
        assertThat(decrypted).isEqualTo(original);
    }

    @Test
    void 같은_원문을_두번_암호화하면_다른_암호문이_나온다() {
        String original = "test@example.com";
        String enc1 = encryptor.convertToDatabaseColumn(original);
        String enc2 = encryptor.convertToDatabaseColumn(original);
        assertThat(enc1).isNotEqualTo(enc2);
    }

    @Test
    void null_입력은_null_반환() {
        assertThat(encryptor.convertToDatabaseColumn(null)).isNull();
        assertThat(encryptor.convertToEntityAttribute(null)).isNull();
    }

    @Test
    void 한글_이름도_암호화_복호화_가능() {
        String name = "홍길동";
        String decrypted = encryptor.convertToEntityAttribute(
                encryptor.convertToDatabaseColumn(name));
        assertThat(decrypted).isEqualTo(name);
    }
}
