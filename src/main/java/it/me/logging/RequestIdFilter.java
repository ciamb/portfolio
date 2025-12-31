package it.me.logging;

import it.me.domain.Header;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import java.util.UUID;
import org.jboss.logging.MDC;

@Provider
@Priority(Priorities.AUTHENTICATION - 1) // before the c-api-key filter
public class RequestIdFilter implements ContainerRequestFilter, ContainerResponseFilter {
    private final String MDC_KEY = "requestId";

    @Override
    public void filter(ContainerRequestContext requestContext) {
        var requestId = requestContext.getHeaderString(Header.C_REQUEST_ID.getValue());
        if (requestId == null || requestId.isBlank()) {
            requestId = UUID.randomUUID().toString();
        }
        MDC.put(MDC_KEY, requestId);
        requestContext.setProperty(MDC_KEY, requestId);
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        var requestId = requestContext.getProperty(MDC_KEY);
        if (requestId != null) {
            responseContext.getHeaders().putSingle(Header.C_REQUEST_ID.getValue(), requestId.toString());
        }
        MDC.remove(MDC_KEY);
    }
}
