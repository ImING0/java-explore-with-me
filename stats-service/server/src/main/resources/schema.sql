drop table if exists stats cascade;

create table stats
(
    id        bigint generated always as identity,
    app       varchar(128) not null,
    uri       varchar(256) not null,
    ip        varchar(64)  not null,
    timestamp timestamp    not null
);