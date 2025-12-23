package it.me.domain.mapper;

import it.me.domain.dto.ContactMe;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
class ContactMeEntityEmailBodyMapperTest {

    @InjectMocks
    private ContactMeEmailBodyMapper sut;

    @Test
    void shouldMapNoError() {
        //given
        ContactMe contactMeA = ContactMe.builder()
                .message("ciao")
                .email("marcella@bella.com")
                .name("marcella")
                .contactBack(true)
                .createdAt(ZonedDateTime.of(2025, 1, 2, 3, 4, 5, 666, ZoneId.of("Europe/Paris")))
                .build();
        List<ContactMe> contactMeList = List.of(contactMeA);

        //when
        String apply = assertDoesNotThrow(() -> sut.apply(contactMeList));

        //then
        assertThat(apply).contains(contactMeA.message());
        assertThat(apply).contains(contactMeA.name());
        assertThat(apply).contains(contactMeA.email());
        assertThat(apply).contains("Vuole essere ricontattato all'indirizzo email marcella@bella.com");
    }

    @Test
    void shouldMapWithError() {
        // given
        ContactMe contactMe = ContactMe.builder()
                .name("marcella")
                .email("marcella@bella.com")
                .message("ciao")
                .contactBack(true)
                .errorReason("Mail provider down")
                .createdAt(ZonedDateTime.now())
                .build();

        List<ContactMe> list = List.of(contactMe);

        // when
        String body = sut.apply(list);

        // then
        assertThat(body).contains("è andato in errore");
        assertThat(body).contains("Mail provider down");
        assertThat(body).contains("Questa è la email per ricontattarlo: marcella@bella.com");
    }
}