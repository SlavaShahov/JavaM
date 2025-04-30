TRUNCATE TABLE users RESTART IDENTITY CASCADE;
TRUNCATE TABLE customers RESTART IDENTITY CASCADE;
TRUNCATE TABLE orders RESTART IDENTITY CASCADE;
TRUNCATE TABLE items RESTART IDENTITY CASCADE;
TRUNCATE TABLE order_details RESTART IDENTITY CASCADE;
TRUNCATE TABLE payments RESTART IDENTITY CASCADE;
TRUNCATE TABLE sessions RESTART IDENTITY CASCADE;
TRUNCATE TABLE banned_tokens RESTART IDENTITY CASCADE;

WITH user_insert AS (
INSERT INTO users (email, login, password, role)
VALUES ('john.smith@example.com', 'johnsmith', 'hashed_password_123', 'USER')
    RETURNING id
    ),
    customer AS (
INSERT INTO customers (id, name, city, street, zipcode)
SELECT user_insert.id, 'John Smith', 'New York', '789 Maple Ave', '10001'
FROM user_insert
    RETURNING id
    )
INSERT INTO sessions (user_id, token, expiration_time)
SELECT user_insert.id, 'token_johnsmith_123', '2025-12-31 23:59:59'
FROM user_insert;

WITH user_insert AS (
INSERT INTO users (email, login, password, role)
VALUES ('alice.brown@example.com', 'alicebrown', 'hashed_password_456', 'USER')
    RETURNING id
    ),
    customer AS (
INSERT INTO customers (id, name, city, street, zipcode)
SELECT user_insert.id, 'Alice Brown', 'San Francisco', '123 Pine St', '94101'
FROM user_insert
    RETURNING id
    )
INSERT INTO sessions (user_id, token, expiration_time)
SELECT user_insert.id, 'token_alicebrown_456', '2025-12-31 23:59:59'
FROM user_insert;

INSERT INTO users (email, login, password, role)
VALUES ('bob.jones@example.com', 'bobjones', 'hashed_password_789', 'ADMIN')
    RETURNING id;