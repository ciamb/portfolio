package it.me.web.error;

import it.me.domain.Header;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;
import org.jboss.logging.MDC;

@Provider
public class IllegalStateExceptionMapper implements ExceptionMapper<IllegalStateException> {
    private static final Logger logger = Logger.getLogger(IllegalStateExceptionMapper.class.getName());

    @Inject
    UriInfo uriInfo;

    @Override
    public Response toResponse(IllegalStateException exception) {
        var requestId = String.valueOf(MDC.get("requestId"));
        var path = uriInfo != null ? uriInfo.getPath() : "";

        var errorResponse = new ErrorResponse(
                "illegal_state_exception",
                exception.getMessage(),
                path,
                requestId
        );

        logger.errorf("%s: %s", errorResponse.error(), exception.getMessage());

        return Response.status(Response.Status.CONFLICT)
                .type(MediaType.APPLICATION_JSON)
                .header(Header.X_REQUEST_ID.getValue(), requestId)
                .entity(errorResponse)
                .build();
    }
}
