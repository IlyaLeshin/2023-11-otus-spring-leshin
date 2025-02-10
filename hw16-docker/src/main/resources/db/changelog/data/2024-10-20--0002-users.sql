--liquibase formatted sql

--changeset IlyaLeshin:2024-10-20-002-users
insert into users(username, password)
values ('admin', '$2a$12$aei29O3Qa2t.Sm1CBuC2oO.I5My2uvdVe7cUf3MYZJI2mY.W1x6D.'),
('user', '$2a$12$W.Kr6TdM8kOWQuv5rmRRyu1LVrD9WZk0ZHF6TiFrdEQ9QWlbibFPa');