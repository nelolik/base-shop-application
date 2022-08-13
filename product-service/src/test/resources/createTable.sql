CREATE TABLE product (
    id INTEGER primary key,
    name VARCHAR(50),
    description VARCHAR(200),
    price NUMERIC(12, 2),
    quantity INTEGER,
    category VARCHAR(20)
);

CREATE SEQUENCE hibernate_sequence;