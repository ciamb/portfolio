package it.me.web.error;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import it.me.domain.Header;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.UriInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;

@ExtendWith(MockitoExtension.class)
class NotFoundExceptionMapperTest {

    @InjectMocks
    private NotFoundExceptionMapper sut;

    @Mock
    UriInfo uriInfo;

    @AfterEach
    void afterEach() {
        MDC.clear();
    }

    @Test
    void toResponse_withUriInfo() {
        // given
        given(uriInfo.getPath()).willReturn("/api/test");

        MDC.put("requestId", "jdnfivuh89w7hv");

        var nfe = new NotFoundException("not found");

        // when
        var result = sut.toResponse(nfe);

        // then
        assertThat(result.getStatus()).isEqualTo(404);
        assertThat(result.getHeaderString(Header.C_REQUEST_ID.getValue())).isEqualTo("jdnfivuh89w7hv");

        var errorResponse = (ErrorResponse) result.getEntity();
        assertThat(errorResponse.error()).isEqualTo("not_found");
        assertThat(errorResponse.message()).contains("not found");
        assertThat(errorResponse.path()).isEqualTo("/api/test");
        assertThat(errorResponse.requestId()).isEqualTo("jdnfivuh89w7hv");
    }

    @Test
    void toResponse_withoutUriInfo() {
        // given
        sut.uriInfo = null;
        MDC.put("requestId", "jdnfivuh89w7hv");

        var nfe = new NotFoundException("not found");

        // when
        var result = sut.toResponse(nfe);

        // then
        assertThat(result.getStatus()).isEqualTo(404);
        assertThat(result.getHeaderString(Header.C_REQUEST_ID.getValue())).isEqualTo("jdnfivuh89w7hv");

        var errorResponse = (ErrorResponse) result.getEntity();
        assertThat(errorResponse.error()).isEqualTo("not_found");
        assertThat(errorResponse.message()).contains("not found");
        assertThat(errorResponse.path()).isEmpty();
        assertThat(errorResponse.requestId()).isEqualTo("jdnfivuh89w7hv");
    }
}
