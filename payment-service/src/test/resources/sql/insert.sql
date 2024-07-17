insert into balances (id, user_id, balance)
values (1, 1, 5000);
insert into balances (id, user_id, balance)
values (2, 2, 2000);
insert into balances (id, user_id, balance)
values (3, 3, 7000);

SELECT setval ('balances_id_seq', (SELECT MAX(id) FROM balances));