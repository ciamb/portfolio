package it.me.domain.service.contact.me;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import io.quarkus.mailer.Mailer;
import it.me.domain.dto.ContactMe;
import it.me.domain.mapper.ContactMeEmailBodyMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ContactMeEmailSenderServiceTest {

    @InjectMocks
    private ContactMeEmailSenderService sut;

    @Mock
    ContactMeEmailBodyMapper contactMeEmailBodyMapper;

    @Mock
    Mailer mailer;

    @Test
    void shouldCallMailer() {
        // given
        var processedContactMe = ContactMe.builder().build();
        List<ContactMe> contactMeList = List.of(processedContactMe);

        given(contactMeEmailBodyMapper.apply(any())).willReturn("body");

        // when
        // them
        assertDoesNotThrow(() -> sut.sendSummaryEmailForPendingList(contactMeList, "email"));
    }
}
