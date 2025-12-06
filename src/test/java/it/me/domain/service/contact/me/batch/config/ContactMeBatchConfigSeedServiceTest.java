package it.me.domain.service.contact.me.batch.config;

import it.me.repository.entity.ContactMeBatchConfigEntity;
import it.me.repository.contact.me.batch.config.ContactMeBatchConfigPersistRepository;
import it.me.repository.contact.me.batch.config.ContactMeBatchConfigReadByIdRepository;
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
    ContactMeBatchConfigReadByIdRepository contactMeBatchConfigReadByIdRepository;

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
        ContactMeBatchConfigEntity config = new ContactMeBatchConfigEntity()
                .setId(1)
                .setIsActive(true)
                .setTargetEmail("myemail");
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
    void foundPresent_shouldNotSeedConfig_butThrowISEOnTargetEmail() throws NoSuchFieldException, IllegalAccessException {
        //given
        setTargetEmailMockViaReflection("targetEmail");
        ContactMeBatchConfigEntity config = new ContactMeBatchConfigEntity()
                .setId(1)
                .setIsActive(true)
                .setTargetEmail("   ");
        given(contactMeBatchConfigReadByIdRepository.readByIdEquals1())
                .willReturn(Optional.of(config));

        //when
        var ise = assertThrows(IllegalStateException.class,
                () -> sut.createContactMeBatchConfigIfMissing());

        //then
        assertTrue(ise.getMessage().contains("target_email"));
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
