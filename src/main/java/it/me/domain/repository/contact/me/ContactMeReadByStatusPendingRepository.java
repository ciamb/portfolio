package it.me.domain.repository.contact.me;

import it.me.domain.dto.ContactMe;

import java.util.List;

public interface ContactMeReadByStatusPendingRepository {
    List<ContactMe> readAllByStatusPending();
}
