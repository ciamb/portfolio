package it.me.domain.service.contact.me.batch.config;

import it.me.domain.dto.ContactMeBatchConfig;
import it.me.domain.repository.contact.me.batch.config.ContactMeBatchConfigPersistRepository;
import it.me.repository.contact.me.batch.config.ContactMeBatchConfigReadByIdRepositoryJpa;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ContactMeBatchConfigSeedServiceTest {
    @InjectMocks
    private ContactMeBatchConfigSeedService sut;

    @Mock
    ContactMeBatchConfigPersistRepository contactMeBatchConfigPersistRepository;

    @Mock
    ContactMeBatchConfigReadByIdRepositoryJpa contactMeBatchConfigReadByIdRepository;

    @Test
    void foundNotPresent_seedConfig() throws NoSuchFieldException, IllegalAccessException {
        //given
        setTargetEmailMockViaReflection("targetEmail");
        given(contactMeBatchConfigReadByIdRepository.readByIdEquals1())
                .willReturn(Optional.empty());

        //when
        assertDoesNotThrow(() -> sut.createContactMeBatchConfigIfMissing());
        //then
        InOrder inOrder = inOrder(contactMeBatchConfigReadByIdRepository, contactMeBatchConfigPersistRepository);
        inOrder.verify(contactMeBatchConfigReadByIdRepository).readByIdEquals1();
        inOrder.verify(contactMeBatchConfigPersistRepository, times(1))
                .persist(Mockito.any());
        inOrder.verifyNoMoreInteractions();
    }


    @Test
    void foundPresent_shouldNotSeedConfig() throws NoSuchFieldException, IllegalAccessException {
        //given
        setTargetEmailMockViaReflection("targetEmail");
        ContactMeBatchConfig config = ContactMeBatchConfig.builder()
                .id(1)
                .isActive(true)
                .targetEmail("myemail")
                .build();
        given(contactMeBatchConfigReadByIdRepository.readByIdEquals1())
                .willReturn(Optional.of(config));

        //when
        assertDoesNotThrow(() -> sut.createContactMeBatchConfigIfMissing());

        //then
        InOrder inOrder = inOrder(contactMeBatchConfigReadByIdRepository);
        inOrder.verify(contactMeBatchConfigReadByIdRepository).readByIdEquals1();
        Mockito.verifyNoInteractions(contactMeBatchConfigPersistRepository);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void foundPresent_shouldNotThrowISEOnTargetEmail() throws NoSuchFieldException, IllegalAccessException {
        //given
        setTargetEmailMockViaReflection("");
        given(contactMeBatchConfigReadByIdRepository.readByIdEquals1())
                .willReturn(Optional.empty());

        //when
        var ise = assertThrows(IllegalStateException.class,
                () -> sut.createContactMeBatchConfigIfMissing());

        //then
        assertTrue(ise.getMessage().contains("Add contact.me.batch.config.target-email"));
        InOrder inOrder = inOrder(contactMeBatchConfigReadByIdRepository);
        inOrder.verify(contactMeBatchConfigReadByIdRepository).readByIdEquals1();
        Mockito.verifyNoInteractions(contactMeBatchConfigPersistRepository);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Set target email mock via reflection for testing purpose
     *
     * @param targetEmailMock target email value to set
     * @throws NoSuchFieldException   possible exception 1
     * @throws IllegalAccessException possible exception 2
     */
    private void setTargetEmailMockViaReflection(String targetEmailMock) throws NoSuchFieldException, IllegalAccessException {
        Field targetEmail = ContactMeBatchConfigSeedService.class.getDeclaredField("targetEmail");
        targetEmail.setAccessible(true);
        targetEmail.set(sut, targetEmailMock);
    }
}
