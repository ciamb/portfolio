package it.me.security;

import io.quarkus.arc.Unremovable;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.IOException;
import java.util.Objects;

@Provider
@Priority(Priorities.AUTHENTICATION)
@Unremovable
public class ApiKeyAuthFilter implements ContainerRequestFilter {

    @ConfigProperty(name = "app.admin.enabled", defaultValue = "false")
    boolean isAdmin;

    @ConfigProperty(name = "app.admin.api-key", defaultValue = "")
    String apiKey;

    @ConfigProperty(name = "app.admin.header", defaultValue = "X-API-Key")
    String header;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        var path = requestContext.getUriInfo().getPath();
        if (!isAdmin || !path.startsWith("/api/admin")) {
            return;
        }

        if (apiKey == null || apiKey.isBlank()) {
            requestContext.abortWith(Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity("{\"error\":\"admin_api_key_not_configured\"}")
                    .build());
            return;
        }

        var provided = requestContext.getHeaderString(header);
        if (!Objects.equals(provided, apiKey)) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .header("WWW-Authenticate", "ApiKey realm=\"admin\"")
                    .entity("{\"error\":\"invalid_api_key\"}")
                    .build());
        }
    }
}
