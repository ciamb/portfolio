package it.me.repository.contact.me;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import it.me.domain.dto.ContactMe;
import it.me.repository.entity.ContactMeEntity;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ContactMeProcessedBulkUpdateRepositoryJpaTest {

    @InjectMocks
    private ContactMeProcessedBulkUpdateRepositoryJpa sut;

    @Mock
    EntityManager em;

    @Test
    void shouldProcess() {
        // given
        List<ContactMe> processed = new ArrayList<>();
        ContactMe cm1 = ContactMe.builder().id(1L).build();
        processed.add(cm1);

        ContactMeEntity cme1 = new ContactMeEntity();
        cme1.setId(1L);

        given(em.find(eq(ContactMeEntity.class), eq(cm1.id()))).willReturn(cme1);

        // when
        sut.updateProcessedContactMe(processed);
        // then
        assertEquals(1, processed.size());
    }

    @Test
    void shouldReturnNull_noId() {
        // given
        List<ContactMe> processed = new ArrayList<>();
        ContactMe cm1 = ContactMe.builder().build();
        processed.add(cm1);

        // when
        sut.updateProcessedContactMe(processed);
        // then
        assertEquals(1, processed.size());
    }

    @Test
    void shouldReturnNull_noEntity() {
        // given
        List<ContactMe> processed = new ArrayList<>();
        ContactMe cm1 = ContactMe.builder().id(1L).build();
        processed.add(cm1);

        given(em.find(eq(ContactMeEntity.class), eq(cm1.id()))).willReturn(null);

        // when
        sut.updateProcessedContactMe(processed);
        // then
        assertEquals(1, processed.size());
    }
}
