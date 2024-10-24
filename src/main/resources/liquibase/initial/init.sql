--liquibase formatted sql

--changeset nuriponzu:init-1
create or replace function update_timestamp()
returns trigger as $$
begin
    new.updated_at = CURRENT_TIMESTAMP;
    return new;
end
$$ language plpgsql;