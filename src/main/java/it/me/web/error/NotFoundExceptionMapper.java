package it.me.web.error;

import it.me.domain.Header;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;
import org.jboss.logging.MDC;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {
    private final Logger logger = Logger.getLogger(NotFoundExceptionMapper.class.getName());

    @Inject
    UriInfo uriInfo;

    @Override
    public Response toResponse(NotFoundException exception) {
        var requestId = String.valueOf(MDC.get("requestId"));
        var path = uriInfo != null ? uriInfo.getPath() : "";

        var errorResponse = new ErrorResponse("not_found", exception.getMessage(), path, requestId);

        logger.errorf("%s: %s", errorResponse.error(), errorResponse.message());

        return Response.status(Response.Status.NOT_FOUND)
                .type(MediaType.APPLICATION_JSON)
                .header(Header.X_REQUEST_ID.getValue(), requestId)
                .entity(errorResponse)
                .build();
    }
}
