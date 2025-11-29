package it.me.domain.service;

import it.me.entity.ContactMeBatchConfig;
import it.me.repository.ContactMeBatchConfigReadByIdRepository;
import it.me.web.dto.request.ContactMeBatchConfigUpdateRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ContactMeBatchConfigUpdateIsActiveServiceTest {
    @InjectMocks
    private ContactMeBatchConfigUpdateIsActiveService sut;

    @Mock
    ContactMeBatchConfigReadByIdRepository contactMeBatchConfigReadByIdRepository;

    @Mock
    ContactMeBatchConfigUpdateRequest contactMeBatchConfigUpdateRequest;

    @Test
    void shouldUpdateConfig_withActiveTrue() {
        // given
        given(contactMeBatchConfigReadByIdRepository.readByIdEquals1())
                .willReturn(Optional.of(new ContactMeBatchConfig()));
        given(contactMeBatchConfigUpdateRequest.isActive()).willReturn(true);

        //when
        Boolean result = assertDoesNotThrow(
                () -> sut.updateContactMeBatchConfig(contactMeBatchConfigUpdateRequest));

        //then
        assertTrue(result);
        Mockito.verify(contactMeBatchConfigReadByIdRepository, Mockito.times(1))
                .readByIdEquals1();
    }

    @Test
    void shouldThrowIllegalException_causeNotFoundConfig() {
        // given
        given(contactMeBatchConfigReadByIdRepository.readByIdEquals1())
                .willReturn(Optional.empty());


        //when
        IllegalStateException ise = assertThrows(IllegalStateException.class,
                () -> sut.updateContactMeBatchConfig(contactMeBatchConfigUpdateRequest));

        //then
        assertTrue(ise.getMessage().contains("Contact Me Batch Config not found"));
        Mockito.verify(contactMeBatchConfigReadByIdRepository, Mockito.times(1))
                .readByIdEquals1();
    }
}