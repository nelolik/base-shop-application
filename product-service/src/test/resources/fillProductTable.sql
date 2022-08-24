DELETE FROM product;

insert into product (id, name, description, price, quantity, category)
    values (nextval('hibernate_sequence'), 'toothpaste', 'gel too clean tooth', 200.3, 22, 'health'),
            (nextval('hibernate_sequence'), 'pen', 'a thing to write on paper', 80, 77, 'office'),
            (nextval('hibernate_sequence'), 'beacon', 'tasty thing', 350, 7, 'food');