package it.me.domain.mapper;

import it.me.domain.dto.ProcessedContactMe;
import it.me.repository.entity.ContactMeEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class ProcessedContactMeEntityMapperTest {

    @InjectMocks
    private ProcessedContactMeMapper sut;

    @Test
    void shouldMap_null() {
        // given
        ContactMeEntity contactMeEntity = null;

        //when
        ProcessedContactMe apply = sut.apply(contactMeEntity);

        //then
        assertNull(apply);
    }

    @Test
    void shouldMap_contactMe() {
        // given
        var contactMe = new ContactMeEntity();

        //when
        ProcessedContactMe apply = sut.apply(contactMe);

        //then
        assertNotNull(apply);
        assertThat(apply).hasAllNullFieldsOrPropertiesExcept(
                "contactBack", "status", "attempts");
    }
}