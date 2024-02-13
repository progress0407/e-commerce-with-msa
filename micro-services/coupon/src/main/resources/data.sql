INSERT INTO coupon (id, dtype, name, discount_amount, discount_percent, expiration_start_at, expiration_end_at, created_at, updated_at)
VALUES (1, 'FixedDiscountCouponEntity', '첫 가입 3,000원 할인 쿠폰', 3000, null, now(), ADDDATE(NOW(), 30), now(), now()),
       (2, 'RatioDiscountCouponEntity', '초신사 생일 15% 할인 쿠폰', null, 15, now(), ADDDATE(NOW(), 30), now(), now());

INSERT INTO user_coupon (id, user_id, coupon_id, is_use, created_at, updated_at)
VALUES (1, 1, 1, false, now(), now()),
       (2, 1, 2, false, now(), now()),
       (3, 2, 1, false, now(), now()),
       (4, 2, 2, false, now(), now());

insert into item_replica (id, item_id, item_amount, created_at, updated_at)
values (1, 1, 49800, now(), now()),
       (2, 2, 49800, now(), now()),
       (3, 3, 49800, now(), now()),
       (4, 4, 245000, now(), now())