--liquibase formatted sql
--changeset postgres:1

create table if not exists id
(
    id              bigint ,
    primary key (id)
);



create table  if not exists connect
(
    link              text    not null,
    id                bigint ,
    updated           timestamp,
    foreign key (id) references id(id) on delete cascade
);





