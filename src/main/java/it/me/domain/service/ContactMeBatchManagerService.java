package it.me.domain.service;

import it.me.entity.ContactMe;
import it.me.entity.ContactMeBatchLog;
import it.me.repository.ContactMeBatchConfigReadByIdRepository;
import it.me.repository.ContactMeBatchLogPersistRepository;
import it.me.repository.ContactMeReadByStatusPendingRepository;
import it.me.web.dto.response.ContactMeBatchResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
public class ContactMeBatchManagerService {
    private final Logger logger = Logger.getLogger(ContactMeBatchManagerService.class.getName());

    @Inject
    ContactMeBatchConfigReadByIdRepository contactMeBatchConfigReadByIdRepository;

    @Inject
    ContactMeReadByStatusPendingRepository contactMeReadByStatusPendingRepository;

    @Inject
    ContactMeEmailSenderService contactMeEmailSenderService;

    @Inject
    ContactMeBatchLogPersistRepository contactMeBatchLogPersistRepository;

    @Transactional
    public ContactMeBatchResponse executeBatch() {
        var contactMeBatchConfig = contactMeBatchConfigReadByIdRepository.readByIdEquals1()
                .orElseThrow(() -> new IllegalStateException("No contact me batch config present"));

        if (!contactMeBatchConfig.isActive()) {
            logger.warnf("Contact me batch config is not active");
            return new ContactMeBatchResponse(contactMeBatchConfig.isActive(), null, 0, 0, null);
        }

        List<ContactMe> found = contactMeReadByStatusPendingRepository.readAllByStatusPending();
        if (found.isEmpty()) {
            logger.warnf("No contact me  in PENDING status");
            return new ContactMeBatchResponse(contactMeBatchConfig.isActive(), null, 0, 0, null);
        }

        ZonedDateTime now = ZonedDateTime.now();
        AtomicInteger processed = new AtomicInteger();
        AtomicInteger withError = new AtomicInteger();

        found.forEach(contactMe -> {
            try {
                processed.getAndIncrement();
                contactMe.setStatus(ContactMe.Status.PROCESSED);
                contactMe.setAttempts(contactMe.attempts() + 1);
                contactMe.setLastAttemptAt(now);
                contactMe.setUpdatedAt(now);
                contactMe.setErrorReason("");
            } catch (Exception e) {
                var errorMsg = e.getMessage().length() > 500
                        ? e.getMessage().substring(0, 500)
                        : e.getMessage();
                withError.getAndIncrement();
                contactMe.setStatus(ContactMe.Status.ERROR);
                contactMe.setAttempts(contactMe.attempts() + 1);
                contactMe.setLastAttemptAt(now);
                contactMe.setUpdatedAt(now);
                contactMe.setErrorReason(errorMsg);
            }
        });


        try {
            contactMeEmailSenderService.sendSummaryEmailForPendingList(found, contactMeBatchConfig.targetEmail());
        } catch (Exception e) {
            withError.set(found.size());
            var errorMsg = e.getMessage().length() > 500
                    ? e.getMessage().substring(0, 500)
                    : e.getMessage();
            found.forEach(cm -> {
                cm.setStatus(ContactMe.Status.ERROR);
                cm.setAttempts(cm.attempts() + 1);
                cm.setLastAttemptAt(now);
                cm.setUpdatedAt(now);
                cm.setErrorReason(errorMsg);
            });
        }

        ContactMeBatchLog log = contactMeBatchLogPersistRepository.persist(new ContactMeBatchLog()
                .setRunAt(now)
                .setProcessed(processed.get())
                .setWithError(withError.get())
                .setSentTo(contactMeBatchConfig.targetEmail()));

        return new ContactMeBatchResponse(
                contactMeBatchConfig.isActive(),
                log.id(),
                processed.get(),
                withError.get(),
                contactMeBatchConfig.targetEmail());
    }
}
