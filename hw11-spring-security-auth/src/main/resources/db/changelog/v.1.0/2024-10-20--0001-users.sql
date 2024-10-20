--liquibase formatted sql

--changeset IlyaLeshin:2024-10-20-003-users
create table users (
    id bigserial not null unique,
    username varchar(255) not null,
    password varchar(255) not null,
    primary key (id)
);