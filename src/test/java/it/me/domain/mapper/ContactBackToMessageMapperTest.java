package it.me.domain.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ContactBackToMessageMapperTest {

    @InjectMocks
    private ContactBackToMessageMapper sut;

    @Test
    void mapContactBack_false_toMessage() {
        // given
        var contactBack = false;

        // when
        String result = sut.apply(contactBack);

        //then
        assertThat(result).contains("Ti auguro una buona giornata!");
    }

    @Test
    void mapContactBack_true_toMessage() {
        // given
        var contactBack = true;

        // when
        String result = sut.apply(contactBack);

        //then
        assertThat(result).contains("Ci sentiamo presto!");
    }
}