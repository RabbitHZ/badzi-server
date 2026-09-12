CREATE TABLE shop_item (
    id                BIGSERIAL PRIMARY KEY,
    name              VARCHAR(100) NOT NULL,
    description       TEXT,
    price_krw         INT          NOT NULL,
    groble_product_id VARCHAR(100),
    active            BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at        TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at        TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE TABLE user_item (
    id            BIGSERIAL PRIMARY KEY,
    user_id       BIGINT NOT NULL REFERENCES users(id),
    shop_item_id  BIGINT NOT NULL REFERENCES shop_item(id),
    payment_id    BIGINT,
    acquired_at   TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_user_item UNIQUE (user_id, shop_item_id)
);

CREATE INDEX idx_user_item_user_id ON user_item (user_id);
