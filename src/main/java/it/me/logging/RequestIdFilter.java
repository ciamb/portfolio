package it.me.logging;

import it.me.domain.Header;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.MDC;

import java.util.UUID;

@Provider
@Priority(Priorities.AUTHENTICATION + 1) // after the X-API-Key filter
public class RequestIdFilter implements ContainerRequestFilter, ContainerResponseFilter {
    private final String MDC_KEY = "requestId";

    @Override
    public void filter(ContainerRequestContext requestContext) {
        var requestId = requestContext.getHeaderString(Header.X_REQUEST_ID.getValue());
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
            responseContext.getHeaders().putSingle(
                    Header.X_REQUEST_ID.getValue(), requestId.toString());
        }
        MDC.remove(MDC_KEY);
    }
}
