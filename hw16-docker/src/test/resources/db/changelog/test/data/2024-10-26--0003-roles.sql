--liquibase formatted sql

--changeset IlyaLeshin:2024-03-08-002-authors-genres-books
insert into roles(name)
values ('TEST_ADMIN'), ('TEST_USER'), ('TEST_CAN_READ_AUTHORS'), ('TEST_CAN_READ_GENRES'),
('TEST_CAN_EDIT_BOOKS'), ('TEST_CAN_EDIT_COMMENTS');

insert into users_roles(user_id, role_id)
values (1, 1), (1, 3), (1, 4), (1, 5), (1, 6),
       (2, 2), (2, 3), (2, 4), (2, 5), (2, 6);