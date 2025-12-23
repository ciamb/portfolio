package it.me.domain.repository.contact.me;

public interface ContactMeCountByEmailAndStatusPendingRepository {
    Long countContactMeByEmailAndStatusPending(String email);
}
