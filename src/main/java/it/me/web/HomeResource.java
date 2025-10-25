package it.me.web;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import it.me.repository.PageContentReadBySlugRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/")
public class HomeResource {

    private final Template index;
    private final PageContentReadBySlugRepository pageContentReadBySlugRepository;

    @Inject
    public HomeResource(Template index, PageContentReadBySlugRepository pageContentReadBySlugRepository) {
        this.index = index;
        this.pageContentReadBySlugRepository = pageContentReadBySlugRepository;
    }

    @GET
    public Response home() {
        var home = pageContentReadBySlugRepository.readBySlug("home");
        TemplateInstance view = index.data("home", home.orElse(null));
        return Response.ok(view).build();
    }
}
