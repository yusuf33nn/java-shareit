DROP TABLE IF EXISTS items cascade;
DROP TABLE IF EXISTS bookings cascade;
DROP TABLE IF EXISTS users cascade;

CREATE TABLE IF NOT EXISTS users
(
    id    bigint generated always as identity primary key,
    name  varchar(255) not null,
    email varchar(255) not null unique
);

CREATE TABLE IF NOT EXISTS items
(
    id           bigint generated always as identity primary key,
    name         varchar(255) not null,
    description  varchar(255) not null,
    is_available boolean      not null,
    owner_id      bigint references users (id)
);

CREATE TABLE IF NOT EXISTS bookings
(
    id         bigint generated always as identity primary key,
    state      varchar(50) not null
        CHECK (state IN ('CURRENT', 'PAST', 'FUTURE', 'WAITING', 'APPROVED', 'REJECTED')),
    item_id    bigint      not null references items (id),
    booker_id  bigint      not null references users (id),
    start_date timestamp   not null,
    end_date   timestamp   not null,
    created_at timestamp   not null default now(),
    updated_at timestamp
);
