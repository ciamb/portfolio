package it.me.domain.service;

import it.me.domain.dto.ProcessedContactMe;
import it.me.domain.dto.ProcessedInfo;
import it.me.domain.mapper.ProcessedContactMeListMapper;
import it.me.entity.ContactMe;
import it.me.repository.ContactMeReadByStatusPendingRepository;
import it.me.repository.ProcessedContactMeBulkUpdateRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
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
    ProcessedContactMeListMapper processedContactMeListMapper;

    @Inject
    ProcessedContactMeBulkUpdateRepository processedContactMeBulkUpdateRepository;

    public ProcessedInfo processPendingContactMe() {
        ZonedDateTime now = ZonedDateTime.now();

        List<ContactMe> found = contactMeReadByStatusPendingRepository.readAllByStatusPending();
        if (found.isEmpty()) {
            logger.warn("No contact me found in PENDING status");
            return new ProcessedInfo(now, 0, 0, Collections.emptyList());
        }

        List<ProcessedContactMe> processingList = processedContactMeListMapper.apply(found);

        AtomicInteger processed = new AtomicInteger();
        AtomicInteger withError = new AtomicInteger();

        List<ProcessedContactMe> processedList = new ArrayList<>();

        processingList.forEach(processing -> {
            try {
                ProcessedContactMe processedContactMe = processing.improve()
                        .status(ContactMe.Status.PROCESSED)
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
                ProcessedContactMe errorContactMe = processing.improve()
                        .status(ContactMe.Status.ERROR)
                        .attempts(processing.attempts() + 1)
                        .lastAttemptAt(now)
                        .updatedAt(now)
                        .errorReason(errorMsg)
                        .build();
                processedList.add(errorContactMe);
                withError.getAndIncrement();
            }
        });

        processedContactMeBulkUpdateRepository.updateProcessedContactMe(processedList);

        return new ProcessedInfo(now, processed.get(), withError.get(), processedList);
    }
}
