package it.me.repository.contact.me.batch.config;

import it.me.repository.entity.ContactMeBatchConfigEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.util.Optional;

@ApplicationScoped
public class ContactMeBatchConfigReadByIdRepository {
    @Inject
    EntityManager em;

    public Optional<ContactMeBatchConfigEntity> readByIdEquals1() {
        return em.createNamedQuery(ContactMeBatchConfigEntity.READ_BY_ID, ContactMeBatchConfigEntity.class)
                .setParameter("id", 1)
                .getResultStream()
                .findFirst();
    }
}
