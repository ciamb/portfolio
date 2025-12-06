package it.me.web.api.page.content;

import it.me.domain.service.page.content.PageContentUpdateBySlugService;
import it.me.web.dto.request.PageContentUpdateRequest;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/admin/page")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PageContentUpdateAdminResource {

    @Inject
    PageContentUpdateBySlugService pageContentUpdateBySlugService;

    @PUT
    @Path("/update/{slug}")
    public Response updatePageContentBySlug(
            @PathParam("slug") String slug,
            PageContentUpdateRequest pageContentUpdateRequest) {
        var updated = pageContentUpdateBySlugService.updatePageContentBySlug(slug, pageContentUpdateRequest);
        return Response.ok(updated).build();
    }

}
