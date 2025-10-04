DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users
(
    id bigint generated always as identity primary key,
    name varchar(255) not null,
    email varchar(255) not null unique
);

CREATE TABLE IF NOT EXISTS items
(
    id bigint generated always as identity primary key,
    name varchar(255) not null,
    description varchar(255) not null,
    is_available boolean not null,
    user_id bigint references users(id)
)