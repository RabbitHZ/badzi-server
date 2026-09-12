package com.bazzi.app.infrastructure.gateway;

import com.bazzi.app.application.gateway.PaymentGateway;
import org.springframework.stereotype.Component;

/**
 * 그로블 판매링크 조립 구현체.
 * 서명 검증은 웹훅 연동 가이드 확보 후 5-11에서 구현 예정.
 */
@Component
public class GrobleGateway implements PaymentGateway {

    private static final String GROBLE_PAYMENT_BASE = "https://groble.im/payment/";

    @Override
    public String buildPaymentUrl(String grobleProductId, String sellerReference) {
        return GROBLE_PAYMENT_BASE + grobleProductId + "?ref=" + sellerReference;
    }
}
