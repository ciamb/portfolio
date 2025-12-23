package it.me.domain.repository.contact.me.batch.log;

import it.me.domain.dto.ContactMeBatchLog;

public interface ContactMeBatchLogPersistRepository {
    ContactMeBatchLog persist(ContactMeBatchLog contactMeBatchLog);
}
