-- new table for config batch
create table if not exists contact_me_batch_config (
    id integer primary key check (id = 1),
    is_active boolean not null default false,
    target_email varchar(255) not null
);