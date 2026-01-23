package it.me.repository.contact.me.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import it.me.domain.dto.ContactMe;
import it.me.repository.entity.ContactMeEntity;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ContactMeEntityList2ContactMeListMapperTest {

    @InjectMocks
    private ContactMeEntityList2ContactMeListMapper sut;

    @Mock
    private ContactMeEntity2ContactMeMapper contactMeEntity2ContactMeMapper;

    @Test
    void shouldReturnNull() {
        // given
        // when
        // then
        assertNull(sut.apply(null));
    }

    @Test
    void shouldReturnEmptyList() {
        // given
        // when
        List<ContactMe> apply = sut.apply(Collections.emptyList());
        // then
        assertThat(apply).isEmpty();
    }

    @Test
    void shouldApplyCorrectly() {
        // given
        List<ContactMeEntity> entities = Collections.singletonList(new ContactMeEntity());
        given(contactMeEntity2ContactMeMapper.apply(any(ContactMeEntity.class)))
                .willReturn(ContactMe.builder().build());
        // when
        List<ContactMe> apply = sut.apply(entities);
        // then
        assertThat(apply.size()).isEqualTo(1);
    }
}
