--liquibase formatted sql

--changeset IlyaLeshin:2024-10-26-001-roles
create table if not exists roles (
    id bigserial not null unique,
    name varchar(255) not null,
    primary key (id)
);

create table if not exists users_roles (
    user_id bigint references users(id) on delete cascade,
    role_id bigint references roles(id) on delete cascade,
    primary key (user_id, role_id)
);