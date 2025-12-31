package it.me.logging;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import it.me.domain.Header;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;

@ExtendWith(MockitoExtension.class)
class RequestIdFilterTest {

    @InjectMocks
    private RequestIdFilter sut;

    @Mock
    ContainerRequestContext requestContext;

    @Mock
    ContainerResponseContext responseContext;

    @Test
    @DisplayName("1. Should put requestId inside MDC")
    void filter_shouldPutRequestIdInMDC() {
        // given request
        given(requestContext.getHeaderString(Header.C_REQUEST_ID.getValue())).willReturn("kfdja8uc381gvbjdvb");
        ArgumentCaptor<String> key = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Object> value = ArgumentCaptor.forClass(Object.class);

        // when request
        sut.filter(requestContext);

        // then request
        verify(requestContext).setProperty(key.capture(), value.capture());
        assertEquals("requestId", key.getValue());
        assertEquals("kfdja8uc381gvbjdvb", value.getValue());
        assertThat(MDC.get("requestId")).isEqualTo("kfdja8uc381gvbjdvb");

        // given response
        given(requestContext.getProperty("requestId")).willReturn(null);

        // when response
        sut.filter(requestContext, responseContext);

        // then response
        assertThat(MDC.get("requestId")).isNull();
    }

    @Test
    @DisplayName("2. Should put requestId inside MDC with UUID generated")
    void filter_shouldPutRequestIdInMDC_withUUIDGenerated_whenRequestIdIsNull() {
        // given request
        given(requestContext.getHeaderString(Header.C_REQUEST_ID.getValue())).willReturn(null);
        ArgumentCaptor<String> key = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Object> value = ArgumentCaptor.forClass(Object.class);

        // when request
        sut.filter(requestContext);

        // then request
        verify(requestContext).setProperty(key.capture(), value.capture());
        assertEquals("requestId", key.getValue());
        assertThat(MDC.get("requestId")).isEqualTo(value.getValue());

        // given response
        given(requestContext.getProperty("requestId")).willReturn(null);

        // when response
        sut.filter(requestContext, responseContext);

        // then response
        assertThat(MDC.get("requestId")).isNull();
    }

    @Test
    @DisplayName("2.1. Should put requestId inside MDC with UUID generated")
    void filter_shouldPutRequestIdInMDC_withUUIDGenerated_whenRequestIdIsBlank() {
        // given request
        given(requestContext.getHeaderString(Header.C_REQUEST_ID.getValue())).willReturn("");
        ArgumentCaptor<String> key = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Object> value = ArgumentCaptor.forClass(Object.class);

        // when request
        sut.filter(requestContext);

        // then request
        verify(requestContext).setProperty(key.capture(), value.capture());
        assertEquals("requestId", key.getValue());
        assertThat(MDC.get("requestId")).isEqualTo(value.getValue());

        // given response
        given(requestContext.getProperty("requestId")).willReturn(null);

        // when response
        sut.filter(requestContext, responseContext);

        // then response
        assertThat(MDC.get("requestId")).isNull();
    }
}
