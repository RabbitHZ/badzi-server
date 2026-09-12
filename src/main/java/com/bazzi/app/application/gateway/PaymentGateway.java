package com.bazzi.app.application.gateway;

public interface PaymentGateway {
    /**
     * 그로블 판매링크를 조립한다.
     * @param grobleProductId 그로블 상품 ID
     * @param sellerReference 서버가 생성한 ref (추적용)
     * @return 구매자에게 전달할 결제 URL
     */
    String buildPaymentUrl(String grobleProductId, String sellerReference);
}
