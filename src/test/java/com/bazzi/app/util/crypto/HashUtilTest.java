package com.bazzi.app.util.crypto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HashUtilTest {

    private HashUtil hashUtil;

    @BeforeEach
    void setUp() {
        hashUtil = new HashUtil("test-salt");
    }

    @Test
    void 같은_입력은_항상_같은_해시() {
        String hash1 = hashUtil.hash("test@example.com");
        String hash2 = hashUtil.hash("test@example.com");
        assertThat(hash1).isEqualTo(hash2);
    }

    @Test
    void 다른_입력은_다른_해시() {
        String hash1 = hashUtil.hash("a@example.com");
        String hash2 = hashUtil.hash("b@example.com");
        assertThat(hash1).isNotEqualTo(hash2);
    }

    @Test
    void 해시_길이는_64자_16진수() {
        String hash = hashUtil.hash("test@example.com");
        assertThat(hash).hasSize(64).matches("[0-9a-f]+");
    }

    @Test
    void null_입력은_null_반환() {
        assertThat(hashUtil.hash(null)).isNull();
    }
}
