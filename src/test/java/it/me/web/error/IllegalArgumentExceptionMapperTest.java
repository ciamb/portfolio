package it.me.web.error;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

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

@ExtendWith(MockitoExtension.class)
class IllegalArgumentExceptionMapperTest {

    @InjectMocks
    private IllegalArgumentExceptionMapper sut;

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
        MDC.put("requestId", "fg43h33grdvberb3453dsfs");

        var iae = new IllegalArgumentException("invalid data");

        // when
        Response result = sut.toResponse(iae);

        // then
        assertThat(result.getStatus()).isEqualTo(400);
        assertThat(result.getHeaderString(Header.C_REQUEST_ID.getValue())).isEqualTo("fg43h33grdvberb3453dsfs");

        var entity = (ErrorResponse) result.getEntity();
        assertThat(entity.error()).isEqualTo("illegal_argument_exception");
        assertThat(entity.message()).isEqualTo("invalid data");
        assertThat(entity.path()).isEqualTo("/api/cv/upload");
        assertThat(entity.requestId()).isEqualTo("fg43h33grdvberb3453dsfs");
    }

    @Test
    void toResponse_withoutUriInfo() {
        // given
        sut.uriInfo = null;

        MDC.put("requestId", "fddfb45h33hj7676w");
        var iae = new IllegalArgumentException("wrong param");

        // when
        Response result = sut.toResponse(iae);

        // then
        assertThat(result.getStatus()).isEqualTo(400);
        assertThat(result.getHeaderString(Header.C_REQUEST_ID.getValue())).isEqualTo("fddfb45h33hj7676w");

        var entity = (ErrorResponse) result.getEntity();
        assertThat(entity.error()).isEqualTo("illegal_argument_exception");
        assertThat(entity.message()).isEqualTo("wrong param");
        assertThat(entity.path()).isEmpty();
        assertThat(entity.requestId()).isEqualTo("fddfb45h33hj7676w");
    }
}
