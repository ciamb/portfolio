package it.me.repository;

import it.me.entity.CvFile;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.time.ZonedDateTime;

@ApplicationScoped
public class CvFileUpdateIsActiveToFalseIfAnyRepository {

    @Inject
    EntityManager em;

    public void updateIsActiveToFalseIfAny() {
        em.createNamedQuery(CvFile.UPDATE_IS_ACTIVE_TO_FALSE_IF_ANY)
                .setParameter("updatedAt", ZonedDateTime.now())
                .executeUpdate();
    }
}
