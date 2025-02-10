--liquibase formatted sql

--changeset IlyaLeshin:2024-10-20-002-users
insert into users(username, password)
values ('testAdmin', '$2a$12$.FKPGFL/McPFVysgwGSIV.GNYrKJsls9LZSSAH9plG/7Da2KWKcWC'),
('testUser', '$2a$12$WV2uof3gftqZxwCpg2vf5OKVL482RGnGqVxTZrgFpYIeuwfLQb3Xu');