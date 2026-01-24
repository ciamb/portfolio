package it.me.web.validator;

import static org.junit.jupiter.api.Assertions.*;

import it.me.domain.PortfolioPublicK;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChatRequestValidatorTest {

    @InjectMocks
    private ChatRequestValidator sut;

    @Test
    void shouldBeInScope() {
        // given
        String inScopeKeyword = PortfolioPublicK.OpenAi.IN_SCOPE[0];
        String userMessage = "asking " + inScopeKeyword;

        // when
        boolean result = sut.isMessageOutOfScope(userMessage);

        // then
        assertFalse(result);
    }

    @Test
    void shouldBeOutOfScope() {
        // given
        String outOfScopeKeyword = PortfolioPublicK.OpenAi.OUT_OF_SCOPE[0];
        String userMessage = "asking " + outOfScopeKeyword;

        // when
        boolean result = sut.isMessageOutOfScope(userMessage);

        // then
        assertTrue(result);
    }

    @Test
    void defaultScope() {
        // given
        String userMessage = "fake message";

        // when
        boolean result = sut.isMessageOutOfScope(userMessage);

        // then
        assertTrue(result);
    }

    @Test
    void defaultScopeWhenNullMessage() {
        // given
        // when
        boolean result = sut.isMessageOutOfScope(null);

        // then
        assertTrue(result);
    }
}
