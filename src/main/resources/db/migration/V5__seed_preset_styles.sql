-- maple shop_item 삽입
INSERT INTO shop_item (name, description, price_krw, groble_product_id, active)
VALUES ('Maple 뱃지 스타일', '메이플스토리 감성의 귀여운 뱃지 스타일', 1900, NULL, TRUE);

-- rabbit shop_item 삽입
INSERT INTO shop_item (name, description, price_krw, groble_product_id, active)
VALUES ('Rabbit 뱃지 스타일', '아기자기한 토끼 감성의 뱃지 스타일', 1900, NULL, TRUE);

-- maple badge_style 시드 (is_preset=true, user_id=null)
INSERT INTO badge_style (user_id, name, style_type, shop_item_id, is_preset)
SELECT NULL, 'Maple', 'maple', id, TRUE
FROM shop_item WHERE name = 'Maple 뱃지 스타일';

-- rabbit badge_style 시드 (is_preset=true, user_id=null)
INSERT INTO badge_style (user_id, name, style_type, shop_item_id, is_preset)
SELECT NULL, 'Rabbit', 'rabbit', id, TRUE
FROM shop_item WHERE name = 'Rabbit 뱃지 스타일';
