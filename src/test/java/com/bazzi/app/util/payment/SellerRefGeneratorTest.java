package com.bazzi.app.util.payment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class SellerRefGeneratorTest {

    private SellerRefGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new SellerRefGenerator();
    }

    @Test
    void 생성된_ref는_128자_이하() {
        String ref = generator.generate();
        assertThat(ref).hasSizeLessThanOrEqualTo(128);
    }

    @Test
    void 허용_문자만_포함() {
        String ref = generator.generate();
        assertThat(ref).matches("[A-Za-z0-9\\-_.:=~]+");
    }

    @Test
    void 매번_다른_값_생성() {
        Set<String> refs = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            refs.add(generator.generate());
        }
        assertThat(refs).hasSize(100);
    }

    @Test
    void ref_길이는_22자_base64url() {
        String ref = generator.generate();
        // UUID 16바이트 → base64url without padding = 22자
        assertThat(ref).hasSize(22);
    }
}
