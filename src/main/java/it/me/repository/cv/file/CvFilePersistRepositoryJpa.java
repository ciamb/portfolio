package it.me.repository.cv.file;

import it.me.domain.dto.CvFile;
import it.me.domain.repository.cv.file.CvFilePersistRepository;
import it.me.repository.cv.file.mapper.CvFile2CvFileEntityMapper;
import it.me.repository.entity.CvFileEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CvFilePersistRepositoryJpa implements CvFilePersistRepository {

    @Inject
    EntityManager em;

    @Inject
    CvFile2CvFileEntityMapper cvFile2CvFileEntityMapper;

    @Transactional
    @Override
    public CvFile persist(CvFile cvFile) {
        CvFileEntity entity = cvFile2CvFileEntityMapper.apply(cvFile);
        em.persist(entity);
        return cvFile;
    }
}
