-- creazione nuova tabella per assistant_profile
create table if not exists assistant_profile (
    id bigint primary key check (id = 1),
    name varchar(20) not null,
    system_prompt text not null,
    fallback_message text not null,
    enabled boolean not null default true,
    created_at timestamptz not null default current_timestamp,
    updated_at timestamptz not null default current_timestamp
)