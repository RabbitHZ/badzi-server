CREATE TABLE payment (
    id                BIGSERIAL PRIMARY KEY,
    user_id           BIGINT       NOT NULL REFERENCES users(id),
    shop_item_id      BIGINT       NOT NULL REFERENCES shop_item(id),
    order_no          VARCHAR(64)  NOT NULL UNIQUE,
    seller_reference  VARCHAR(128) NOT NULL UNIQUE,
    amount_krw        INT          NOT NULL,
    status            VARCHAR(20)  NOT NULL DEFAULT 'PENDING',
    pg_tx_id          VARCHAR(200),
    paid_at           TIMESTAMP WITH TIME ZONE,
    created_at        TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at        TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_payment_user_id ON payment (user_id);
CREATE INDEX idx_payment_seller_reference ON payment (seller_reference);
