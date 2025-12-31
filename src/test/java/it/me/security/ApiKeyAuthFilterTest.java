package it.me.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ApiKeyAuthFilterTest {

    @InjectMocks
    private ApiKeyAuthFilter sut;

    @Mock
    ContainerRequestContext requestContext;

    @Mock
    UriInfo uriInfo;

    @Test
    @DisplayName("1. Filter a non admin path")
    void filter_nonAdmin() throws IOException {
        // given
        sut.isAdmin = false;
        given(requestContext.getUriInfo()).willReturn(uriInfo);
        given(uriInfo.getPath()).willReturn("/api/test/double");

        // when
        sut.filter(requestContext);

        // then
        verify(requestContext, never()).abortWith(any());
    }

    @Test
    @DisplayName("2. Filter admin path with api-key not configured")
    void filter_adminWithBlankApiKey() throws IOException {
        // given
        sut.isAdmin = true;
        sut.apiKey = "";
        sut.header = "c-api-key";

        given(requestContext.getUriInfo()).willReturn(uriInfo);
        given(uriInfo.getPath()).willReturn("/api/admin/test/double");

        ArgumentCaptor<Response> response = ArgumentCaptor.forClass(Response.class);

        // when
        sut.filter(requestContext);

        // then
        verify(requestContext).getUriInfo();
        verify(requestContext).abortWith(response.capture());
        assertThat(response.getValue().getStatus()).isEqualTo(Response.Status.SERVICE_UNAVAILABLE.getStatusCode());
    }

    @Test
    @DisplayName("3. Filter admin path with wrong api-key ")
    void filter_adminWithWrongApiKey() throws IOException {
        // given
        sut.isAdmin = true;
        sut.apiKey = "wrong_apikey";
        sut.header = "c-api-key";

        given(requestContext.getUriInfo()).willReturn(uriInfo);
        given(uriInfo.getPath()).willReturn("/api/admin/test/double");
        given(requestContext.getHeaderString("c-api-key")).willReturn("very_wrong_apikey");

        ArgumentCaptor<Response> response = ArgumentCaptor.forClass(Response.class);

        // when
        sut.filter(requestContext);

        // then
        verify(requestContext).getUriInfo();
        verify(requestContext).abortWith(response.capture());
        assertThat(response.getValue().getStatus()).isEqualTo(Response.Status.UNAUTHORIZED.getStatusCode());
    }
}
