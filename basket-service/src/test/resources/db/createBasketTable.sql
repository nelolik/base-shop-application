DROP TABLE basket IF EXISTS;
DROP SEQUENCE basket_id_sequence IF EXISTS;

CREATE TABLE basket (
    basket_id INTEGER NOT NULL,
    product_id INTEGER NOT NULL,
    product_name VARCHAR(50),
    product_price NUMERIC(12,2),
    quantity INTEGER,
    CONSTRAINT pk PRIMARY KEY (basket_id, product_id)
);

CREATE SEQUENCE basket_id_sequence;