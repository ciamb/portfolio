package it.me.web.api.health;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.CacheControl;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Endpoint da pingare per vedere se l'app e attiva o nel caso non lo sia
 * (il piano Free di render ddisattiva il servizio ogni 15 minuiti)
 * parte l'attivazione
 */
@Path("/api/health")
public class HealthResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response health() {
        CacheControl cc = new CacheControl();
        cc.setNoCache(true);
        cc.setNoStore(true);
        return Response.ok("Sono up!").cacheControl(cc).build();
    }
}
