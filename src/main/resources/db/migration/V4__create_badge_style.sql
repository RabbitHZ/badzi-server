CREATE TABLE badge_style (
    id           BIGSERIAL PRIMARY KEY,
    user_id      BIGINT REFERENCES users(id),
    name         VARCHAR(100) NOT NULL,
    style_type   VARCHAR(50)  NOT NULL,
    color        VARCHAR(20),
    label        VARCHAR(100),
    icon         VARCHAR(100),
    font_size    INT,
    shop_item_id BIGINT REFERENCES shop_item(id),
    is_preset    BOOLEAN NOT NULL DEFAULT FALSE,
    created_at   TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_badge_style_user_id ON badge_style (user_id);
CREATE INDEX idx_badge_style_is_preset ON badge_style (is_preset);
