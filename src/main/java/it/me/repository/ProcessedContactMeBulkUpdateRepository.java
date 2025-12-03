package it.me.repository;

import it.me.domain.dto.ProcessedContactMe;
import it.me.entity.ContactMe;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
public class ProcessedContactMeBulkUpdateRepository {
    private final Logger logger = Logger.getLogger(ProcessedContactMeBulkUpdateRepository.class.getName());

    @Inject
    EntityManager em;

    @Transactional
    public void updateProcessedContactMe(List<ProcessedContactMe> processedContactMe) {
        List<ContactMe> updated = processedContactMe.stream()
                .map(pcm -> {
                    if (pcm.id() == null) {
                        logger.warnf("Anomalia, Processed contact me senza id %s", pcm);
                        return null;
                    }

                    var entity = em.find(ContactMe.class, pcm.id());
                    if (entity == null) {
                        logger.warnf("Nessuna entita trovata con id %d", pcm.id());
                        return null;
                    }

                    entity.setStatus(pcm.status());
                    entity.setAttempts(pcm.attempts());
                    entity.setLastAttemptAt(pcm.lastAttemptAt());
                    entity.setUpdatedAt(pcm.updatedAt());
                    entity.setErrorReason(pcm.errorReason());
                    return entity;
                })
                .toList();
        logger.infof("Processed contact me aggiornate: %d", updated.size());
    }
}
