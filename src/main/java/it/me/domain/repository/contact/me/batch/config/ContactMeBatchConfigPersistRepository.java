package it.me.domain.repository.contact.me.batch.config;

import it.me.domain.dto.ContactMeBatchConfig;

public interface ContactMeBatchConfigPersistRepository {
    ContactMeBatchConfig persist(ContactMeBatchConfig contactMeBatchConfig);
}
