create table users
(
    id                bigserial primary key,
    username          varchar(255) not null,
    password          varchar(255) not null,
    user_type         varchar(255) not null,
    real_name         varchar(255) not null,
    document_type     varchar(255) not null,
    document_number   varchar(255) not null,
    phone_number      varchar(255) not null,
    user_level        varchar(255) not null,
    bio               varchar(255) not null,
    region            varchar(255) not null,
    district          varchar(255) not null,
    country           varchar(255) not null,
    registration_time timestamp    not null,
    update_time       timestamp    not null
);