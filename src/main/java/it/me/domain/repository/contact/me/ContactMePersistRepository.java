package it.me.domain.repository.contact.me;

import it.me.domain.dto.ContactMe;

public interface ContactMePersistRepository {
    ContactMe persist(ContactMe contactMe);
}
