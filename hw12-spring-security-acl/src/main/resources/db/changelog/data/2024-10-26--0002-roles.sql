--liquibase formatted sql

--changeset IlyaLeshin:2024-03-08-002-authors-genres-books
merge into roles(name)
key (name)
values ('ADMIN'), ('USER'), ('CAN_READ_AUTHORS'), ('CAN_READ_GENRES'), ('CAN_READ_BOOKS'),
('CAN_EDIT_BOOKS'), ('CAN_EDIT_COMMENTS');

merge into users_roles(user_id, role_id)
key(user_id, role_id)
values (1, 1), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7),
       (2, 2), (2, 3), (2, 4), (2, 5), (2, 7);