package it.me.web.error;

import it.me.domain.Header;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.*;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;
import org.jboss.logging.MDC;


@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {
    private static final Logger logger = Logger.getLogger(GlobalExceptionMapper.class.getName());

    @Inject
    UriInfo uriInfo;

    @Inject
    Request request;

    @Override
    public Response toResponse(Throwable exception) {
        var requestId = String.valueOf(MDC.get("requestId"));
        var path = uriInfo != null ? uriInfo.getPath() : "";
        var method = request != null ? request.getMethod() : "?";

        logger.errorf(exception, "Unhandled error on %s %s [requestId=%s]", method, path, requestId);

        var errorResponse = new ErrorResponse(
                "internal_error", "Unhandled error", path, requestId);

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON)
                .header(Header.X_REQUEST_ID.getValue(), requestId)
                .entity(errorResponse)
                .build();
    }
}
