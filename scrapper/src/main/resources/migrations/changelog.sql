--liquibase formatted sql
--changeset postgres:1

create table if not exists id
(
    id              bigint generated always as identity,
    primary key (id)
);

create table  if not exists links
(
    link              text  not null,
    primary key (link)
);

create table  if not exists connect
(
    link              text    not null,
    id                bigint generated always as identity,
    foreign key (link) references links(link),
    foreign key (id) references id(id)
);





