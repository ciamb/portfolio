-- create table form page content
create table if not exists page_content (
    id         bigserial primary key,
    slug       varchar(64)  not null unique,
    title      varchar(120) not null,
    subtitle   varchar(240),
    body       TEXT,
    updated_at timestamp with time zone not null default now()
);