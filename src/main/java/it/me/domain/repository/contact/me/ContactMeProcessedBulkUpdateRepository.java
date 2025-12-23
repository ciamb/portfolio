package it.me.domain.repository.contact.me;

import it.me.domain.dto.ContactMe;

import java.util.List;

public interface ContactMeProcessedBulkUpdateRepository {
    void updateProcessedContactMe(List<ContactMe> processedContactMe);
}
