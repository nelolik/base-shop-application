DROP TABLE user_view_statistic IF EXISTS;

CREATE TABLE user_view_statistic (
    user_id INTEGER,
    product_id INTEGER,
    visit_count INTEGER,
    PRIMARY KEY (user_id, product_id)
);