
DROP TYPE IF EXISTS event_state cascade;
DROP TYPE IF EXISTS request_status cascade;
DROP TABLE IF EXISTS categories cascade;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS events CASCADE ;
DROP TABLE IF EXISTS requests CASCADE ;
DROP TABLE IF EXISTS compilations CASCADE ;
create type event_state as enum ('PENDING', 'PUBLISHED', 'CANCELED');
create type request_status as enum ('PENDING', 'CONFIRMED', 'REJECTED');

create table if not exists categories
(
    id         bigint generated always as identity,
    name       varchar(50) not null,
    created_on timestamp   not null,
    constraint categories_id_pk
        primary key (id),
    constraint categories_name_pk
        unique (name),
    constraint check_name_min_length
        check (length((name)::text) >= 1)
);

comment on column categories.created_on is 'Дата и время создания категории. Возможно будет полезно в будещем.';

create table if not exists users
(
    name       varchar(250) not null,
    email      varchar(254) not null,
    id         bigint generated always as identity,
    created_on timestamp    not null,
    constraint users_id_pk
        primary key (id),
    constraint users_email_pk
        unique (email),
    constraint check_name_email_min_length
        check ((length((name)::text) >= 2) AND (length((email)::text) >= 6))
);

comment on column users.created_on is 'Дата и время регистрации(создания) пользователя';

comment on constraint check_name_email_min_length on users is 'проверка на мин длину имени и почты. Для почты 6, для имени 2';

create table if not exists compilations
(
    id         bigint generated always as identity,
    title      varchar(50)           not null,
    pinned     boolean default false not null,
    created_on timestamp             not null,
    constraint compilations_id_pk
        primary key (id),
    constraint check_title_min_length
        check (length((title)::text) >= 1)
);

comment on column compilations.id is 'Идентификатор компиляции';

comment on column compilations.title is 'Заголовок подборки  ';

comment on column compilations.pinned is 'Закреплена ли подборка на главной странице сайта';

comment on column compilations.created_on is 'Дата и время создания подборки. Полезно в будещем. ';

comment on constraint check_title_min_length on compilations is 'Провекар минимальной длины заголовка';

create table if not exists events
(
    annotation         varchar(2000)         not null,
    category_id        integer               not null,
    id                 bigint generated always as identity,
    description        varchar(7000)         not null,
    event_date         timestamp             not null,
    lat                double precision      not null,
    lon                double precision      not null,
    paid               boolean default false not null,
    participant_limit  integer default 0     not null,
    request_moderation boolean default true  not null,
    title              varchar(120)          not null,
    created_on         timestamp             not null,
    initiator_id       bigint                not null,
    published_on       timestamp,
    state              event_state           not null,
    compilation_id     bigint,
    constraint events_id_pk
        primary key (id),
    constraint "events_initiator_Id_users_id_fk"
        foreign key (initiator_id) references users
            on update cascade on delete cascade,
    constraint events_compilation_id_compilations_id_fk
        foreign key (compilation_id) references compilations
            on update cascade on delete set null,
    constraint events_category_categories_id_fk
        foreign key (category_id) references categories
            on update cascade on delete restrict,
    constraint check_annotation_min_length
        check (length((annotation)::text) >= 20),
    constraint check_description_min_length
        check (length((description)::text) >= 20),
    constraint check_title_min_length
        check (length((title)::text) >= 3)
);

comment on column events.annotation is 'Краткое описание события';

comment on column events.category_id is 'Идентификатор категории';

comment on column events.id is 'Идентификатор события';

comment on column events.description is 'Полное описание события';

comment on column events.event_date is 'Дата и время на которые намечено событие. Дата и время указываются в формате "yyyy-MM-dd HH:mm:ss"';

comment on column events.lat is 'Широта';

comment on column events.lon is 'Долгота';

comment on column events.paid is 'Нужно ли оплачивать участие в событии  ';

comment on column events.participant_limit is 'Ограничение на количество участников. Значение 0 - означает отсутствие ограничения';

comment on column events.request_moderation is 'Нужна ли пре-модерация заявок на участие. Если true, то все заявки будут ожидать подтверждения инициатором события. Если false - то будут подтверждаться автоматически.';

comment on column events.title is 'Заголовок события';

comment on column events.created_on is 'Дата и время создания события (в формате "yyyy-MM-dd HH:mm:ss")';

comment on column events.initiator_id is 'Идентификатор организатора события';

comment on column events.published_on is 'Дата и время публикации события (в формате "yyyy-MM-dd HH:mm:ss")';

comment on column events.state is 'Список состояний жизненного цикла события';

comment on column events.compilation_id is 'Идентификатор подборки, в которую включено текущее событие. Может быть null';

comment on constraint "events_initiator_Id_users_id_fk" on events is 'Связывает организатора события с таблицей пользователй';

comment on constraint events_compilation_id_compilations_id_fk on events is 'Внешний ключ связывающий поле compilation_id с таблицей compilations';

create table if not exists requests
(
    id           bigint generated always as identity,
    requester_id bigint         not null,
    event_id     bigint         not null,
    created_on   timestamp      not null,
    status       request_status not null,
    constraint requests_id_pk
        primary key (id),
    constraint requests_requester_id_users_id_fk
        foreign key (requester_id) references users
            on update cascade on delete cascade,
    constraint requests_event_id_events_id_fk
        foreign key (event_id) references events
);

comment on table requests is 'Запросы пользователей на участие в событии';

comment on column requests.id is 'Идентификатор заявки';

comment on column requests.requester_id is 'Идентификатор пользователя, отправившего заявку';

comment on column requests.event_id is 'Идентификатор события';

comment on column requests.created_on is 'Дата и время создания заявки';

comment on column requests.status is 'Статус заявки';

comment on constraint requests_requester_id_users_id_fk on requests is 'Ограничение внешнего ключа по таблице пользователей';

comment on constraint requests_event_id_events_id_fk on requests is 'Ограничение по внешнему ключу id таблицы event';

