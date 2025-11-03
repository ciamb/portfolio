package it.me.repository;

import it.me.entity.CvFile;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.util.Optional;

@ApplicationScoped
public class CvFileReadBySha256Repository {

    @Inject
    EntityManager em;

    public Optional<CvFile> readBySha256(String sha256) {
        return em.createNamedQuery(CvFile.READ_BY_SHA256, CvFile.class)
                .setParameter("sha256", sha256)
                .getResultStream()
                .findFirst();
    }
}
