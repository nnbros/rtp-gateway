--liquibase formatted sql

--changeset guronas:init-5
create table if not exists gateway.state_machine (
    machine_id varchar(127) primary key,
    state varchar(127),
    state_machine_context bigint
);