package it.me.domain.service;

import io.quarkus.mailer.Mailer;
import it.me.domain.dto.ProcessedContactMe;
import it.me.domain.mapper.ContactMeEmailBodyMapper;
import it.me.entity.ContactMe;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

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
        var processedContactMe = ProcessedContactMe.builder().build();
        List<ProcessedContactMe> contactMeList = List.of(processedContactMe);

        given(contactMeEmailBodyMapper.apply(any())).willReturn("body");

        //when
        //them
        assertDoesNotThrow(() -> sut.sendSummaryEmailForPendingList(contactMeList, "email"));
    }
}