package it.me.repository.cv.file;

import it.me.repository.entity.CvFileEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.util.Optional;

@ApplicationScoped
public class CvFileReadBySha256Repository {

    @Inject
    EntityManager em;

    public Optional<CvFileEntity> readBySha256(String sha256) {
        return em.createNamedQuery(CvFileEntity.READ_BY_SHA256, CvFileEntity.class)
                .setParameter("sha256", sha256)
                .getResultStream()
                .findFirst();
    }
}
