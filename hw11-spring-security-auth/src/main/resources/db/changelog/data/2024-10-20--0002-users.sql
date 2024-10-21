--liquibase formatted sql

--changeset IlyaLeshin:2024-10-20-002-users
merge into users(username, password)
key(username)
values ('user1', '$2a$12$KQZtdFV4vtml5IKzvYpWreewsKvl6cWjVylmJ025Aqz/T8VS8UDSi'),
('user2', '$2a$12$2s3mj6h7PFcws1TydeJRDeapRMe/pgxAfqh0HIJ0c13gUCgK0PC2K');