package it.me.repository;

import it.me.entity.CvFile;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class CvFilePersistRepository {

    @Inject
    EntityManager em;

    public CvFile persistCvFile(CvFile cvFile) {
        em.persist(cvFile);
        em.flush();
        em.refresh(cvFile);
        return cvFile;
    }
}
