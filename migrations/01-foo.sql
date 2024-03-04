create table id
(
    id              bigint generated always as identity,
    primary key (id)
);

create table links
(
    link              text,
    primary key (link)
);

create table connect
(
    link              text,
    id                bigint generated always as identity,
    foreign key (link) references links(link)
    foreign key (id) references id(id)
);





