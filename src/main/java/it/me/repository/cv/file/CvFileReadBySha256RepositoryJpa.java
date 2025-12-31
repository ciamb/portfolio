package it.me.repository.cv.file;

import it.me.domain.dto.CvFile;
import it.me.domain.repository.cv.file.CvFileReadBySha256Repository;
import it.me.repository.cv.file.mapper.CvFileEntity2CvFileMapper;
import it.me.repository.entity.CvFileEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import java.util.Optional;

@ApplicationScoped
public class CvFileReadBySha256RepositoryJpa implements CvFileReadBySha256Repository {
    @Inject
    EntityManager em;

    @Inject
    CvFileEntity2CvFileMapper cvFileEntity2CvFileMapper;

    public Optional<CvFile> readBySha256(String sha256) {
        return em.createNamedQuery(CvFileEntity.READ_BY_SHA256, CvFileEntity.class)
                .setParameter("sha256", sha256)
                .getResultStream()
                .findFirst()
                .map(cvFileEntity2CvFileMapper);
    }
}
