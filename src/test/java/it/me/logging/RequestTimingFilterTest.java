package it.me.logging;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.core.UriInfo;
import java.io.IOException;
import java.net.URI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RequestTimingFilterTest {

    @InjectMocks
    private RequestTimingFilter sut;

    @Mock
    UriInfo uriInfo;

    @Mock
    ContainerRequestContext requestContext;

    @Mock
    ContainerResponseContext responseContext;

    @Test
    @DisplayName("1. Filter is disabled, should do nothing")
    void filter_disabled() throws IOException {
        // given
        sut.isTimingEnabled = false;

        // when
        sut.filter(requestContext);
        sut.filter(requestContext, responseContext);

        // then
        Mockito.verifyNoInteractions(responseContext);
        Mockito.verify(requestContext, never()).setProperty(eq("requestStartAt"), any());
    }

    @Test
    @DisplayName("2. Filter is enabled, should add property")
    void filter_enabled() throws IOException {
        // given
        sut.isTimingEnabled = true;

        // when
        sut.filter(requestContext);

        // then
        Mockito.verify(requestContext, times(1)).setProperty(eq("requestStartAt"), any());
    }

    @Test
    @DisplayName("3. Filter is enabled, should add property and write message")
    void filter_enabled_shouldWriteLogMessage() throws IOException {
        // given
        sut.isTimingEnabled = true;
        sut.slowThresholdMs = 250;

        given(uriInfo.getRequestUri()).willReturn(URI.create("https://localhost/api/test"));
        given(requestContext.getMethod()).willReturn("GET");
        given(responseContext.getStatus()).willReturn(200);

        // when fast
        // then fast
        var startFast = System.nanoTime() - 100 * 1_000_000;
        given(requestContext.getProperty("requestStartAt")).willReturn(startFast);
        sut.filter(requestContext, responseContext);

        // when slow
        // then slow
        var startSlow = System.nanoTime() - 400 * 1_000_000;
        given(requestContext.getProperty("requestStartAt")).willReturn(startSlow);
        sut.filter(requestContext, responseContext);
    }
}
