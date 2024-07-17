insert into users (id, first_name, last_name, email, password, role)
values (1, 'User', 'User', 'user@mail.ru', '$2a$10$rsDTk5fmyyzN8t.I0r0UNezGW.fd9yhQPOtmPjTOZvWrll/C7XiH6', 0);
insert into users (id, first_name, last_name, email, password, role)
values (2, 'Admin', 'Admin', 'admin@mail.ru', '$2a$10$rsDTk5fmyyzN8t.I0r0UNezGW.fd9yhQPOtmPjTOZvWrll/C7XiH6', 0);

SELECT setval ('users_id_seq', (SELECT MAX(id) FROM users));