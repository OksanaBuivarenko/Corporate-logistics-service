insert into products (id, name, count, price)
values (1, 'name1', 2, 200);
insert into products (id, name, count, price)
values (2, 'name2', 2, 300);
insert into products (id, name, count, price)
values (3, 'name3', 2, 400);

SELECT setval ('products_id_seq', (SELECT MAX(id) FROM products));

