package it.me.web.error;

import it.me.domain.Header;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;
import org.slf4j.MDC;

import java.util.stream.Collectors;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    private final Logger logger = Logger.getLogger(ConstraintViolationExceptionMapper.class.getName());

    @Inject
    UriInfo uriInfo;

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        var requestId = String.valueOf(MDC.get("requestId"));
        var path = uriInfo != null ? uriInfo.getPath() : null;

        var message = exception.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .sorted()
                .collect(Collectors.joining("; "));
        if (message.isBlank()) {
            message = "Validation error";
        }

        var errorResponse = new ErrorResponse("bad_request", message, path, requestId);

        logger.errorf("%s: %s", errorResponse.error(), errorResponse.message());

        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .header(Header.C_REQUEST_ID.getValue(), requestId)
                .entity(errorResponse)
                .build();
    }
}
