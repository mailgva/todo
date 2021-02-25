/* Test data */
insert into users(id, username, password, token)
values (1000001, 'admin', '{noop}password', 'eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6ImFkbWluIn0.Kb-r0a7eq6Ef045ziL6iH9phPTm03sJyc5jkUlazyjg');

insert into todo(id, date_time, task, executed, user_id)
values (1000001, '2021-02-24 10:00:00', 'Task 1 for 02-24', true, 1000001),
       (1000002, '2021-02-24 10:15:00', 'Task 2 for 02-24', false, 1000001),
       (1000003, '2021-02-24 13:20:00', 'Task 3 for 02-24', false, 1000001),
       (1000004, '2021-02-25 11:45:00', 'Task 1 for 02-25', true, 1000001),
       (1000005, '2021-02-25 14:50:00', 'Task 2 for 02-25', false, 1000001);
