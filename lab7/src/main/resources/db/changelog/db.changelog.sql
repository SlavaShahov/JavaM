--liquibase formatted sql

-- changeset yourname:001_create_tables

CREATE TABLE customers (
   id  BIGINT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
   name VARCHAR(255) NOT NULL,
   city VARCHAR(255) NOT NULL,
   street VARCHAR(255) NOT NULL,
   zipcode VARCHAR(20) NOT NULL
);

CREATE TABLE orders (
    id  BIGINT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    date TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL,
    customer_id BIGINT NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers(id)
);

CREATE TABLE items (
   id  BIGINT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
   shipping_weight DECIMAL NOT NULL,
   description TEXT NOT NULL,
   measurement_name VARCHAR(255) NOT NULL,
   measurement_symbol VARCHAR(50) NOT NULL
);

CREATE TABLE order_details (
   id  BIGINT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
   quantity_value INT NOT NULL,
   quantity_unit VARCHAR(50),
   quantity_description VARCHAR(255),
   tax_status VARCHAR(50) NOT NULL,
   order_id BIGINT NOT NULL,
   item_id BIGINT NOT NULL,
   FOREIGN KEY (order_id) REFERENCES orders(id),
   FOREIGN KEY (item_id) REFERENCES items(id)
);

CREATE TABLE payments (
  id  BIGINT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
  amount FLOAT NOT NULL,
  payment_type VARCHAR(50) NOT NULL,
  cash_tendered FLOAT,
  name VARCHAR(255),
  bank_id VARCHAR(50),
  credit_number VARCHAR(50),
  credit_type VARCHAR(50),
  exp_date TIMESTAMP,
  order_id BIGINT NOT NULL,
  FOREIGN KEY (order_id) REFERENCES orders(id)
);



-- rollback drop table customers, orders, items, order_details, payments;