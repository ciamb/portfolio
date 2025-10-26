package it.me.web;

import it.me.domain.PageContentUpdateService;
import it.me.web.dto.PageContentUpdateRequest;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/admin/pages")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PageContentUpdateAdminResource {

    @Inject
    PageContentUpdateService  pageContentUpdateService;

    @PUT
    @Path("/{slug}")
    public Response updatePageContentBySlug(
            @PathParam("slug") String slug,
            @Valid PageContentUpdateRequest pageContentUpdateRequest) {
        var updated = pageContentUpdateService.updatePageContentBySlug(slug, pageContentUpdateRequest);
        return Response.ok(updated).build();
    }

}
