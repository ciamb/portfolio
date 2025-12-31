package it.me.logging;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

@Provider
@Priority(Priorities.USER)
public class RequestTimingFilter implements ContainerRequestFilter, ContainerResponseFilter {
    private static final Logger logger = Logger.getLogger(RequestTimingFilter.class.getName());

    @ConfigProperty(name = "app.timing.enabled", defaultValue = "true")
    boolean isTimingEnabled;

    @ConfigProperty(name = "app.timing.slow-threshold-ms", defaultValue = "250")
    long slowThresholdMs;

    @Inject
    UriInfo uriInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if (!isTimingEnabled) {
            return;
        }
        requestContext.setProperty("requestStartAt", System.nanoTime());
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
            throws IOException {
        if (!isTimingEnabled) {
            return;
        }

        var requestStartedAt = requestContext.getProperty("requestStartAt");
        if (!(requestStartedAt instanceof Long startedAt)) {
            return;
        }

        long elapsedInNanos = System.nanoTime() - startedAt;
        long elapsedInMillis = elapsedInNanos / 1_000_000;

        var method = requestContext.getMethod();
        var requestUri = uriInfo.getRequestUri();
        int status = responseContext.getStatus();

        var formattedMessage = String.format("%s %s -> %d in %d ms", method, requestUri, status, elapsedInMillis);

        if (elapsedInMillis >= slowThresholdMs) {
            logger.warn(formattedMessage);
        } else {
            logger.info(formattedMessage);
        }
    }
}
