drop table if exists pandev;

create table if not exists pandev
(
    id serial primary key,
    name varchar(255) not null,
    parent_id integer references pandev(id)
);