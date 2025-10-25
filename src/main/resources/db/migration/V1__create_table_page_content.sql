-- create table form page content
create table if not exists page_content (
    id         integer,
    slug       varchar(64)  not null,
    title      varchar(120) not null,
    subtitle   varchar(240),
    body       TEXT,
    updated_at timestamp,
    primary key (id)
);

-- slug unique constraint
create unique index if not exists uk_page_content_slug
    on page_content (slug);