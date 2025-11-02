-- creazione della tabella per il cv
create table if not exists cv_file (
    id bigserial primary key,
    filename TEXT not null,
    content_type varchar(100) not null default 'application/pdf',
    file_data BYTEA not null,
    filesize_bytes bigint generated always as (octet_length(file_data)) STORED,
    sha256 varchar(64) not null,
    is_active boolean not null default false,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
);

-- evita duplicati esatti con lo stesso contenuto
create unique index if not exists uq_cv_file_sha256 on cv_file(sha256);

-- garantisce che ci sia un solo cv attivo alla volta presente in db
create unique index if not exists uq_single_active_cv on cv_file(is_active) where is_active = true;
