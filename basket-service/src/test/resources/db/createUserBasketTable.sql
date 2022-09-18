DROP TABLE user_basket IF EXISTS;
DROP SEQUENCE user_basket_pk_seq IF EXISTS;

CREATE SEQUENCE user_basket_pk_seq;

CREATE TABLE user_basket (
    basket_id INTEGER NOT NULL DEFAULT nextval('user_basket_pk_seq')  primary key,
    user_id INTEGER NOT NULL,
    creation_time TIMESTAMP WITH TIME ZONE
);

