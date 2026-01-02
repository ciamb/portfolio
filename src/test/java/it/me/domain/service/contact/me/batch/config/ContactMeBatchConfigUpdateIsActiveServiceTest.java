package it.me.domain.service.contact.me.batch.config;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import it.me.domain.repository.contact.me.batch.config.ContactMeBatchConfigUpdateRepository;
import it.me.web.dto.request.ContactMeBatchConfigUpdateRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ContactMeBatchConfigUpdateIsActiveServiceTest {
    @InjectMocks
    private ContactMeBatchConfigUpdateIsActiveService sut;

    @Mock
    ContactMeBatchConfigUpdateRepository contactMeBatchConfigUpdateRepository;

    @Mock
    ContactMeBatchConfigUpdateRequest contactMeBatchConfigUpdateRequest;

    @Test
    void shouldUpdateConfig_withActiveTrue() {
        // given
        given(contactMeBatchConfigUpdateRequest.isActive()).willReturn(true);
        given(contactMeBatchConfigUpdateRepository.update(eq(true))).willReturn(1);

        // when
        Boolean b = assertDoesNotThrow(() -> sut.updateContactMeBatchConfig(contactMeBatchConfigUpdateRequest));

        // then
        Mockito.verify(contactMeBatchConfigUpdateRepository, Mockito.times(1)).update(eq(true));
        assertTrue(b);
    }
}
