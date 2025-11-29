package it.me.web.error;

import it.me.domain.Header;
import jakarta.ws.rs.core.Request;
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
class GlobalExceptionMapperTest {

    @InjectMocks
    private GlobalExceptionMapper sut;

    @Mock
    UriInfo uriInfo;

    @Mock
    Request request;

    @AfterEach
    void afterEach() {
        MDC.clear();
    }

    @Test
    void toResponse_withUriInfo_andRequest() {
        // given
        given(uriInfo.getPath()).willReturn("/api/cv/upload");
        given(request.getMethod()).willReturn("POST");
        MDC.put("requestId", "sjiovn989082029ireuivwisnhf029");

        var re = new RuntimeException("unexpected");

        // when
        Response result = sut.toResponse(re);

        // then
        assertThat(result.getStatus()).isEqualTo(500);
        assertThat(result.getHeaderString(Header.C_REQUEST_ID.getValue()))
                .isEqualTo("sjiovn989082029ireuivwisnhf029");

        var entity = (ErrorResponse) result.getEntity();
        assertThat(entity.error()).isEqualTo("internal_error");
        assertThat(entity.message()).isEqualTo("Unhandled error");
        assertThat(entity.path()).isEqualTo("/api/cv/upload");
        assertThat(entity.requestId()).isEqualTo("sjiovn989082029ireuivwisnhf029");
    }

    @Test
    void toResponse_withoutUriInfo_andRequest() {
        // given
        sut.uriInfo = null;
        sut.request = null;

        MDC.put("requestId", "djhc23874y93094093jinif2");
        var e = new Exception("generic exception");

        // when
        Response resp = sut.toResponse(e);

        // then
        assertThat(resp.getStatus()).isEqualTo(500);
        assertThat(resp.getHeaderString(Header.C_REQUEST_ID.getValue()))
                .isEqualTo("djhc23874y93094093jinif2");

        var entity = (ErrorResponse) resp.getEntity();
        assertThat(entity.error()).isEqualTo("internal_error");
        assertThat(entity.message()).isEqualTo("Unhandled error");
        assertThat(entity.path()).isEmpty();
        assertThat(entity.requestId()).isEqualTo("djhc23874y93094093jinif2");
    }
}