--liquibase formatted sql

--changeset IlyaLeshin:2024-03-08-001-authors-genres-books

create sequence if not exists author_seq start with 1 increment by 1;
create sequence if not exists genre_seq start with 1 increment by 1;
create sequence if not exists book_seq start with 1 increment by 1;

create table if not exists authors (
    id bigserial not null unique,
    full_name varchar(255) not null,
    primary key (id)
);

create table if not exists genres (
    id bigserial not null unique,
    name varchar(255) not null,
    primary key (id)
);

create table if not exists books (
    id bigserial not null unique,
    title varchar(255) not null,
    author_id bigint references authors (id) on delete cascade,
    primary key (id)
);

create table if not exists books_genres (
    book_id bigint references books(id) on delete cascade,
    genre_id bigint references genres(id) on delete cascade,
    primary key (book_id, genre_id)
);