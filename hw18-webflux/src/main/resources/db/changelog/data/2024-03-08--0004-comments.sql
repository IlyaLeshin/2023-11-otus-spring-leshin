--liquibase formatted sql

--changeset IlyaLeshin:2024-03-08-002-comments
merge into comments(text, book_id)
key(text, book_id)
values ('Comment_1', 1), ('Comment_2', 1), ('Comment_3', 1),
       ('Comment_4', 2), ('Comment_5', 2), ('Comment_6', 2);