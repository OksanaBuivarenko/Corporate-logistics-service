insert into orders (id, cost,creation_time,description,destination_address,modified_time,receiver_name,status,user_id)
values (1, 100, '2024-07-10 12:26:15.709358', 'description 1', 'address 1', '2024-07-10 12:26:15.709358', 'Name1', 'registered', 1);
insert into orders (id, cost,creation_time,description,destination_address,modified_time,receiver_name,status,user_id)
values (2, 100, '2024-07-10 12:26:15.709358', 'description 2', 'address 2', '2024-07-10 12:28:15.709358','Name2', 'paid', 2);
insert into orders (id, cost,creation_time,description,destination_address,modified_time,receiver_name,status,user_id)
values (3, 100, '2024-07-10 12:26:15.709358', 'description 3', 'address 3', '2024-07-10 12:26:15.709358','Name3', 'registered', 2);

SELECT setval ('orders_id_seq', (SELECT MAX(id) FROM orders));

insert into status_history (id, creation_time,order_id,status)
values (1, '2024-07-10 12:26:15.709358', 2, 'registered');
insert into status_history (id, creation_time,order_id,status)
values (2, '2024-07-10 12:28:15.709358', 2, 'paid');

SELECT setval ('status_history_id_seq', (SELECT MAX(id) FROM status_history));