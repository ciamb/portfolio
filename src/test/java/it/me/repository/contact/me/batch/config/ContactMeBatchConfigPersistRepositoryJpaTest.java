package it.me.repository.contact.me.batch.config;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;

import it.me.domain.dto.ContactMeBatchConfig;
import it.me.repository.contact.me.batch.config.mapper.ContactMeBatchConfig2ContactMeBatchConfigEntityMapper;
import it.me.repository.entity.ContactMeBatchConfigEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ContactMeBatchConfigPersistRepositoryJpaTest {
    @InjectMocks
    private ContactMeBatchConfigPersistRepositoryJpa sut;

    @Mock
    ContactMeBatchConfig2ContactMeBatchConfigEntityMapper mapper;

    @Mock
    EntityManager em;

    @Test
    @DisplayName("1. Should call persist flush refresh")
    void shouldCallFlushRefresh() {
        // given
        var config = ContactMeBatchConfig.builder().build();
        ContactMeBatchConfigEntity entity = new ContactMeBatchConfigEntity();
        given(mapper.apply(config)).willReturn(entity);

        // when
        ContactMeBatchConfig result = sut.persist(config);

        // then
        assertSame(config, result);
        var inOrder = inOrder(em, mapper);
        inOrder.verify(mapper).apply(config);
        inOrder.verify(em).persist(entity);
        inOrder.verifyNoMoreInteractions();
    }
}
