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

create table comments (
    id bigserial not null unique,
    text varchar(255) not null,
    book_id bigint references books (id) on delete cascade,
    primary key (id)
);