drop table if exists users;
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

drop table if exists place_seekers;
create table place_seekers
(
    seeker_id          bigserial primary key,
    user_id            bigint references users (id) on delete cascade,
    destination_type   varchar(255) not null,
    seeker_title       varchar(255) not null,
    seeker_description text         not null,
    max_expected_price int          not null,
    seeker_expiry_date timestamp    not null,
    create_time        timestamp    not null,
    update_time        timestamp    not null,
    status             varchar(255) not null
);

drop table if exists welcome_offers;
create table welcome_offers
(
    offer_id          bigserial primary key,
    user_id           bigint references users (id) on delete cascade,
    seeker_id   bigint references place_seekers (seeker_id) on delete cascade,
    offer_description text         not null,
    create_time timestamp not null,
    update_time timestamp not null,
    status            varchar(255) not null
);

drop table if exists seek_place_deals;
create table seek_place_deals
(
    deal_id      bigserial primary key,
    seeker_id    bigint references place_seekers (seeker_id) on delete cascade,
    offer_id     bigint references welcome_offers (offer_id) on delete cascade,
    deal_date    timestamp not null,
    seeker_price int       not null,
    offer_price  int       not null
);
