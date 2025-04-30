truncate table customers restart identity cascade;
truncate table orders restart identity cascade;
truncate table items restart identity cascade;
truncate table order_details restart identity cascade;
truncate table payments restart identity cascade;

WITH customer AS (
INSERT INTO customers (name, city, street, zipcode)
VALUES ('John Smith', 'New York', '789 Maple Ave', '10001')
    RETURNING id
    ),
    order_insert AS (
INSERT INTO orders (date, status, customer_id)
SELECT '2025-04-01 14:30:00', 'CONFIRMED', id FROM customer
    RETURNING id
    ),
    item AS (
INSERT INTO items (shipping_weight, description, measurement_name, measurement_symbol)
VALUES (1.5, 'Gadget Pro', 'Weight', 'kg')
    RETURNING id
    ),
    order_detail AS (
INSERT INTO order_details (quantity_value, quantity_unit, quantity_description, tax_status, order_id, item_id)
SELECT 2, 'шт', 'Гаджет Про', 'TAXABLE', order_insert.id, item.id
FROM order_insert, item
    RETURNING id
    )
INSERT INTO payments (amount, payment_type, cash_tendered, name, bank_id, credit_number, credit_type, exp_date, order_id)
SELECT 300.0, 'DEBIT', NULL, NULL, NULL, '9876-5432-1098-7654', 'MASTERCARD', '2028-04-01 14:30:00', order_insert.id
FROM order_insert;

WITH customer AS (
INSERT INTO customers (name, city, street, zipcode)
VALUES ('Alice Brown', 'San Francisco', '123 Pine St', '94101')
    RETURNING id
    ),
    order_insert AS (
INSERT INTO orders (date, status, customer_id)
SELECT '2025-05-10 09:15:00', 'SHIPPED', id FROM customer
    RETURNING id
    ),
    item AS (
INSERT INTO items (shipping_weight, description, measurement_name, measurement_symbol)
VALUES (3.2, 'Deluxe Chair', 'Weight', 'kg')
    RETURNING id
    ),
    order_detail AS (
INSERT INTO order_details (quantity_value, quantity_unit, quantity_description, tax_status, order_id, item_id)
SELECT 1, 'шт', 'Делюкс стул', 'NON_TAXABLE', order_insert.id, item.id
FROM order_insert, item
    RETURNING id
    )
INSERT INTO payments (amount, payment_type, cash_tendered, name, bank_id, credit_number, credit_type, exp_date, order_id)
SELECT 450.0, 'CASH', 500.0, 'Alice Brown', NULL, NULL, NULL, NULL, order_insert.id
FROM order_insert;
