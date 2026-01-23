package it.me.domain.service.contact.me;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;

import it.me.domain.dto.ContactMe;
import it.me.domain.dto.ProcessedInfo;
import it.me.domain.repository.contact.me.ContactMeProcessedBulkUpdateRepository;
import it.me.domain.repository.contact.me.ContactMeReadByStatusPendingRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ContactMeProcessingServiceTest {

    @InjectMocks
    private ContactMeProcessingService sut;

    @Mock
    ContactMeProcessedBulkUpdateRepository contactMeProcessedBulkUpdateRepository;

    @Mock
    ContactMeReadByStatusPendingRepository contactMeReadByStatusPendingRepository;

    @Test
    void shouldProcessCorrectly() {
        // given
        List<ContactMe> pendingList = new ArrayList<>();
        ContactMe contactMe1 = ContactMe.builder().attempts(0).build();
        pendingList.add(contactMe1);
        given(contactMeReadByStatusPendingRepository.readAllByStatusPending()).willReturn(pendingList);

        // when
        ProcessedInfo processedInfo = sut.processPendingContactMe();

        // then
        assertNotNull(processedInfo);
        assertThat(processedInfo.processed()).isEqualTo(1);
        assertThat(processedInfo.processedContactMe().size()).isEqualTo(1);

        Mockito.verify(contactMeReadByStatusPendingRepository, times(1)).readAllByStatusPending();
    }
}
