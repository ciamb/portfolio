-- new table for the batch log
create table if not exists contact_me_batch_log
(
    id bigserial primary key,
    run_at timestamp with time zone not null,
    processed int not null,
    with_error int not null,
    sent_to varchar(255)
);

-- comment
comment on column contact_me_batch_log.processed is 'number of contact_me processed';
comment on column contact_me_batch_log.with_error is 'number of contact_me processed with error';