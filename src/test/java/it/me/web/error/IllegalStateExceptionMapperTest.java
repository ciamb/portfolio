package it.me.web.error;

import it.me.domain.Header;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.jboss.logging.MDC;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class IllegalStateExceptionMapperTest {

    @InjectMocks
    private IllegalStateExceptionMapper sut;

    @Mock
    UriInfo uriInfo;

    @AfterEach
    void afterEach() {
        MDC.clear();
    }

    @Test
    void toResponse_withUriInfo() {
        // given
        given(uriInfo.getPath()).willReturn("/api/cv/upload");
        MDC.put("requestId", "jrfhv8734vuiievr9u38");

        var ise = new IllegalStateException("error");
        Response result = sut.toResponse(ise);

        assertThat(result.getStatus()).isEqualTo(409);
        assertThat(result.getHeaderString(Header.X_REQUEST_ID.getValue())).isEqualTo("jrfhv8734vuiievr9u38");

        var errorResponse = (ErrorResponse) result.getEntity();
        assertThat(errorResponse.error()).isEqualTo("illegal_state_exception");
        assertThat(errorResponse.message()).isEqualTo("error");
        assertThat(errorResponse.path()).isEqualTo("/api/cv/upload");
        assertThat(errorResponse.requestId()).isEqualTo("jrfhv8734vuiievr9u38");
    }

    @Test
    void toResponse_withoutUriInfo() {
        sut.uriInfo = null;
        MDC.put("requestId", "kfjv89euh89vnei");
        var ise = new IllegalStateException("error");

        Response resp = sut.toResponse(ise);

        assertThat(resp.getStatus()).isEqualTo(409);
        assertThat(resp.getHeaderString(Header.X_REQUEST_ID.getValue()))
                .isEqualTo("kfjv89euh89vnei");

        var errorResponse = (ErrorResponse) resp.getEntity();
        assertThat(errorResponse.path()).isEmpty();
        assertThat(errorResponse.error()).isEqualTo("illegal_state_exception");
        assertThat(errorResponse.message()).isEqualTo("error");
        assertThat(errorResponse.requestId()).isEqualTo("kfjv89euh89vnei");
    }
}