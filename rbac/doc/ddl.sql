create table user
(
    id       bigint       not null identity primary key,
    role_id  bigint       not null,
    username varchar(100) not null,
    password varchar(100) not null,
    enabled  boolean      not null
);

create table role
(
    id   bigint       not null identity primary key,
    name varchar(100) not null
);

create table authority
(
    id        bigint       not null identity primary key,
    role_id   bigint       not null,
    authority varchar(100) not null
);