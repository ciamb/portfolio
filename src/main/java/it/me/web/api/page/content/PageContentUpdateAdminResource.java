package it.me.web.api.page.content;

import it.me.domain.dto.PageContent;
import it.me.domain.service.page.content.PageContentUpdateBySlugService;
import it.me.web.dto.request.PageContentUpdateRequest;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

@Path("/api/admin/page")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PageContentUpdateAdminResource {
    private final Logger logger = Logger.getLogger(PageContentUpdateAdminResource.class);

    @Inject
    PageContentUpdateBySlugService pageContentUpdateBySlugService;

    @PUT
    @Path("/update/{slug}")
    public Response updatePageContentBySlug(
            @PathParam("slug") String slug, @Valid PageContentUpdateRequest pageContentUpdateRequest) {
        logger.infof("Received request /api/v1/admin/page/update/%s", slug);
        PageContent updated = pageContentUpdateBySlugService.updatePageContentBySlug(slug, pageContentUpdateRequest);
        return Response.ok(updated).build();
    }
}
