create table users(
    id bigserial primary key,
    username varchar not null,
    password_hash varchar not null
)