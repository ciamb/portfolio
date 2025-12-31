package it.me.web.error;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import it.me.domain.Header;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import java.util.Set;
import org.jboss.logging.MDC;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ConstraintViolationExceptionMapperTest {

    @InjectMocks
    private ConstraintViolationExceptionMapper sut;

    @Mock
    UriInfo uriInfo;

    @AfterEach
    void afterEach() {
        MDC.clear();
    }

    @Test
    void toResponse_withViolations_andUriInfo() {
        // given
        given(uriInfo.getPath()).willReturn("/api/cv/upload");
        MDC.put("requestId", "fkm9v0023433h498c92j");

        ConstraintViolation<?> fieldA = mock(ConstraintViolation.class);
        ConstraintViolation<?> fieldB = mock(ConstraintViolation.class);
        given(fieldA.getMessage()).willReturn("fieldA invalid");
        given(fieldB.getMessage()).willReturn("fieldB required");

        var cve = new ConstraintViolationException(Set.of(fieldA, fieldB));

        // when
        Response result = sut.toResponse(cve);

        // then
        assertThat(result.getStatus()).isEqualTo(400);
        assertThat(result.getHeaderString(Header.C_REQUEST_ID.getValue())).isEqualTo("fkm9v0023433h498c92j");

        var entity = (ErrorResponse) result.getEntity();
        assertThat(entity.error()).isEqualTo("bad_request");
        assertThat(entity.message()).isEqualTo("fieldA invalid; fieldB required");
        assertThat(entity.path()).isEqualTo("/api/cv/upload");
        assertThat(entity.requestId()).isEqualTo("fkm9v0023433h498c92j");
    }

    @Test
    void toResponse_noViolations_handlesNullUriInfo() {
        // given
        sut.uriInfo = null;
        MDC.put("requestId", "8928gn2unfjdnwijfnjdnwi");

        var cve = new ConstraintViolationException(Set.of());

        // when
        Response result = sut.toResponse(cve);

        // then
        assertThat(result.getStatus()).isEqualTo(400);
        assertThat(result.getHeaderString(Header.C_REQUEST_ID.getValue())).isEqualTo("8928gn2unfjdnwijfnjdnwi");

        var entity = (ErrorResponse) result.getEntity();
        assertThat(entity.error()).isEqualTo("bad_request");
        assertThat(entity.message()).isEqualTo("Validation error");
        assertThat(entity.path()).isNull();
        assertThat(entity.requestId()).isEqualTo("8928gn2unfjdnwijfnjdnwi");
    }

    @Test
    void toResponse_blankMessages_becomeValidationError() {
        // given
        sut.uriInfo = null;
        MDC.put("requestId", "cjri9c839828fu239hf2");

        ConstraintViolation<?> fieldA = mock(ConstraintViolation.class);
        given(fieldA.getMessage()).willReturn("  ");

        var cve = new ConstraintViolationException(Set.of(fieldA));

        // when
        Response result = sut.toResponse(cve);

        // then
        var entity = (ErrorResponse) result.getEntity();
        assertThat(entity.message()).isEqualTo("Validation error");
    }
}
