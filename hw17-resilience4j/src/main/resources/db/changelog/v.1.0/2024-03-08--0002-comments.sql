--liquibase formatted sql

--changeset IlyaLeshin:2024-03-08-001-comments
create table comments (
    id bigserial not null unique,
    text varchar(255) not null,
    book_id bigint references books (id) on delete cascade,
    primary key (id)
);