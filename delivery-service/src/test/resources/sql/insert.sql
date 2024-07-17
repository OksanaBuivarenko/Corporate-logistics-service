insert into deliveries (id, order_id, receiver_name, destination_address, status)
values (1, 1, 'Name 1', 'address 1',  true);
insert into deliveries (id, order_id, receiver_name, destination_address, status)
values (2, 1, 'Name 2', 'address 2', false);

SELECT setval ('deliveries_id_seq', (SELECT MAX(id) FROM deliveries));

