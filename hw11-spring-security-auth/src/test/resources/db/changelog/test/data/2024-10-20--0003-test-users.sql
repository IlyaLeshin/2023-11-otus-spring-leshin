--liquibase formatted sql

--changeset IlyaLeshin:2024-10-20-002-users
merge into users(username, password)
key(username)
values ('testUser', 'testPassword');