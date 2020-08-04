create table blog_user
(
    id         bigserial          not null,
    username   varchar(50) unique not null,
    first_name varchar(80)        not null,
    last_name  varchar(80)        not null,
    password   varchar(80)        not null,
    email      varchar(50) unique not null,
    created_at timestamp          not null,
    enabled    boolean            not null,
    primary key (id)
);

-- create type article_status as ENUM ('PUBLIC', 'DRAFT');

create table articles
(
    id         bigserial           not null,
    created_at timestamp           not null,
    full_text  text                not null,
    status     varchar(255)      not null,
    title      varchar(255) unique not null,
    updated_at timestamp,
    author_id  bigint              not null,
    PRIMARY KEY (id),
    FOREIGN KEY (author_id) REFERENCES blog_user (id)
        ON UPDATE cascade ON DELETE set null
);

create table comments
(
    id         bigserial not null,
    message    text      not null,
    created_at timestamp not null,
    article_id bigint    not null,
    author_id  bigint    not null,
    primary key (id),
    foreign key (author_id) references blog_user (id)
        on update cascade on delete cascade,
    foreign key (article_id) references articles (id)
        on update cascade on delete cascade
);

create table tags
(
    id   bigserial           not null,
    name varchar(255) unique not null,
    primary key (id)
);

create table articles_tags
(
    article_id bigint not null,
    tag_id     bigint not null,
    foreign key (article_id) references articles (id)
        on update cascade on delete cascade,
    foreign key (tag_id) references tags (id)
        on update cascade on delete cascade,
    primary key (article_id, tag_id)
);

create table roles
(
    id   bigserial      not null,
    role varchar unique not null,
    primary key (id)
);

create table user_roles
(
    user_id bigint not null,
    role_id bigint not null,
    primary key (user_id, role_id),
    foreign key (user_id) references blog_user (id)
        on update cascade on delete cascade,
    foreign key (role_id) references roles (id)
        on update cascade on delete cascade
)