package it.me.repository;

import it.me.entity.ContactMeBatchConfig;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.util.Optional;

@ApplicationScoped
public class ContactMeBatchConfigReadByIdRepository {
    @Inject
    EntityManager em;

    public Optional<ContactMeBatchConfig> readByIdEquals1() {
        return em.createNamedQuery(ContactMeBatchConfig.READ_BY_ID, ContactMeBatchConfig.class)
                .setParameter("id", 1)
                .getResultStream()
                .findFirst();
    }
}
