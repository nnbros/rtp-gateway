--liquibase formatted sql

--changeset nuriponzu:init-2
create type user_status as enum ('ACTIVE', 'BANNED');

--changeset nuriponzu:init-3
create table if not exists gateway.user (
    id bigint primary key,
    username varchar(255) not null unique,
    status user_status not null default 'ACTIVE',
    created_at timestamp not null default now(),
    updated_at timestamp not null default now(),
    last_action varchar(127) not null default 'user_registration',
    last_action_timestamp timestamp not null default now()
);

--changeset nuriponzu:init-4
create trigger update_timestamp_trigger
before update on gateway.user
for each row
execute function update_timestamp();