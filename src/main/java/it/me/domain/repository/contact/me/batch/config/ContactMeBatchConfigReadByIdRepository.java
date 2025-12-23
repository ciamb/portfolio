package it.me.domain.repository.contact.me.batch.config;

import it.me.domain.dto.ContactMeBatchConfig;

import java.util.Optional;

public interface ContactMeBatchConfigReadByIdRepository {
    Optional<ContactMeBatchConfig> readByIdEquals1();
}
