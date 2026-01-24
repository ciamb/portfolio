package it.me.domain.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

import it.me.domain.service.cv.knowledge.CvKnowledgeProvider;
import it.me.web.dto.request.ChatRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChatRequestInputMapperTest {

    @InjectMocks
    private ChatRequestInputMapper sut;

    @Mock
    CvKnowledgeProvider cvKnowledgeProvider;

    @Mock
    ChatRequest chatRequestMock;

    @Test
    void shouldApplyCorrectly() {
        // given
        given(chatRequestMock.message()).willReturn("message");
        given(cvKnowledgeProvider.getCvFromResources()).willReturn("cv info");

        // when
        String apply = sut.apply(chatRequestMock);

        // then
        assertThat(apply).contains("message");
        assertThat(apply).contains("cv info");
    }

    @Test
    void shouldThrowException_whenMessageIsNull() {
        // given
        given(chatRequestMock.message()).willReturn(null);

        // when
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class, () -> sut.apply(chatRequestMock));
        // then
        assertEquals("Invalid chat message", iae.getMessage());
    }

    @Test
    void shouldThrowException_whenChatRequestIsNull() {
        // given

        // when
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class, () -> sut.apply(null));
        // then
        assertEquals("chatRequest is null", iae.getMessage());
    }
}
