package it.me.web.api.chat;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import it.me.domain.service.chat.AskChatService;
import it.me.web.dto.request.ChatRequest;
import it.me.web.validator.ChatRequestValidator;
import jakarta.ws.rs.core.Response;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChatResourceTest {

    @InjectMocks
    private ChatResource sut;

    @Mock
    AskChatService askChatService;

    @Mock
    ChatRequestValidator validator;

    @Mock
    ChatRequest chatRequest;

    @Test
    void shouldFailValidation() {
        // given
        given(chatRequest.message()).willReturn("message");
        given(validator.isMessageOutOfScope(anyString())).willReturn(CompletableFuture.completedStage(true));

        // when
        CompletionStage<Response> promiseRes = sut.askChat(chatRequest);

        // then
        assertThat(promiseRes).isCompleted();
        assertThat(promiseRes)
                .isCompletedWithValueMatching(
                        response -> Response.Status.BAD_REQUEST.getStatusCode() == response.getStatus())
                .succeedsWithin(5, TimeUnit.SECONDS);
    }

    @Test
    void shouldCallService() {
        // given
        given(chatRequest.message()).willReturn("message");
        given(validator.isMessageOutOfScope(anyString())).willReturn(CompletableFuture.completedStage(false));
        given(askChatService.askChat(chatRequest)).willReturn(CompletableFuture.completedFuture("answer"));

        // when
        CompletionStage<Response> promiseRes = sut.askChat(chatRequest);

        // then
        assertThat(promiseRes).isCompleted();
        assertThat(promiseRes)
                .isCompletedWithValueMatching(res -> res.getEntity().equals("answer"))
                .succeedsWithin(5, TimeUnit.SECONDS);
    }
}
