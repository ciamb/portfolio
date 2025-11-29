package it.me.domain.service;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import it.me.domain.mapper.ContactMeEmailBodyMapper;
import it.me.entity.ContactMe;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.time.ZonedDateTime;
import java.util.List;

@ApplicationScoped
public class ContactMeEmailSenderService {
    private final Logger logger = Logger.getLogger(ContactMeEmailSenderService.class);
    @Inject
    ContactMeEmailBodyMapper contactMeEmailBodyMapper;

    @Inject
    Mailer mailer;

    void sendSummaryEmailForPendingList(List<ContactMe> pendingList, String targetEmail) {
        var emailBody = contactMeEmailBodyMapper.apply(pendingList);
        var emailSubject = "Riepilogo dei messaggi Contact Me al %s".formatted(ZonedDateTime.now());
        logger.infof("Sending email via quarkus mailer");
        mailer.send(Mail.withText(targetEmail, emailSubject, emailBody));
    }
}
