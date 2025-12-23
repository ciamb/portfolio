package it.me.domain.service.contact.me;

import it.me.domain.dto.ContactMe;
import it.me.domain.dto.ProcessedInfo;
import it.me.domain.repository.contact.me.ContactMeProcessedBulkUpdateRepository;
import it.me.domain.repository.contact.me.ContactMeReadByStatusPendingRepository;
import it.me.repository.entity.ContactMeEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
public class ContactMeProcessingService {
    private final Logger logger = Logger.getLogger(ContactMeProcessingService.class.getName());

    @Inject
    ContactMeReadByStatusPendingRepository contactMeReadByStatusPendingRepository;

    @Inject
    ContactMeProcessedBulkUpdateRepository contactMeProcessedBulkUpdateRepository;

    public ProcessedInfo processPendingContactMe() {
        ZonedDateTime now = ZonedDateTime.now();

        List<ContactMe> pendingList = contactMeReadByStatusPendingRepository
                .readAllByStatusPending();

        if (pendingList.isEmpty()) {
            logger.warn("No contact me found in PENDING status");
            return new ProcessedInfo(now, 0, 0, Collections.emptyList());
        }

        AtomicInteger processed = new AtomicInteger();
        AtomicInteger withError = new AtomicInteger();

        List<ContactMe> processedList = new ArrayList<>();

        pendingList.forEach(processing -> {
            try {
                ContactMe processedContactMe = processing.builderFromThis()
                        .status(ContactMeEntity.Status.PROCESSED)
                        .attempts(processing.attempts() + 1)
                        .lastAttemptAt(now)
                        .updatedAt(now)
                        .errorReason("")
                        .build();
                processedList.add(processedContactMe);
                processed.incrementAndGet();
            } catch (Exception e) {
                var errorMsg = e.getMessage() != null && e.getMessage().length() > 500
                        ? e.getMessage().substring(0, 500)
                        : e.getMessage();
                ContactMe erroredContactMe = processing.builderFromThis()
                        .status(ContactMeEntity.Status.ERROR)
                        .attempts(processing.attempts() + 1)
                        .lastAttemptAt(now)
                        .updatedAt(now)
                        .errorReason(errorMsg)
                        .build();
                processedList.add(erroredContactMe);
                withError.getAndIncrement();
            }
        });

        contactMeProcessedBulkUpdateRepository.updateProcessedContactMe(processedList);

        return new ProcessedInfo(now, processed.get(), withError.get(), processedList);
    }
}
