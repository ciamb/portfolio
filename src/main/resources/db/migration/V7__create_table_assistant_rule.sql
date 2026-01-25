-- creazione nuovo tipo regole
create type rule_scope as enum ('IN_SCOPE', 'OUT_OF_SCOPE');

-- creazione tabella per regole assistente
create table if not exists assistant_rule (
    id bigserial primary key,
    assistant_id bigint not null references assistant_profile(id) on delete cascade,
    rule_type rule_scope not null,
    keyword varchar(64) not null,
    priority integer not null default 1,
    enabled boolean not null default true,
    created_at timestamptz not null default current_timestamp,
    updated_at timestamptz not null default current_timestamp
);

-- unique su campi multipli
create unique index if not exists uq_assistant_id_rule_type_keyword
    on assistant_rule(assistant_id, rule_type, keyword);
