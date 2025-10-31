package it.me.web;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import it.me.repository.PageContentReadBySlugRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Path("/")
public class HomeResource {
    private static final DateTimeFormatter IT_DATETIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withLocale(Locale.ITALY);

    private final Template index;
    private final PageContentReadBySlugRepository pageContentReadBySlugRepository;

    @Inject
    public HomeResource(Template index, PageContentReadBySlugRepository pageContentReadBySlugRepository) {
        this.index = index;
        this.pageContentReadBySlugRepository = pageContentReadBySlugRepository;
    }

    @GET
    public Response home() {
        var home = pageContentReadBySlugRepository.readBySlug("home")
                .orElseThrow(() -> new NotFoundException("Page slug=%s not found".formatted("home")));

        var metaTitle = home.title();

        var body = home.body();
        var metaDescription = "";
        if (body != null && !body.isBlank()) {
            metaDescription = body.replaceAll("\\s+", " ").strip();
            if (metaDescription.length() > 160) {
                metaDescription = metaDescription.substring(0, 157).concat("...");
            }
        }

        var updatedAt = home.updatedAt() != null ? IT_DATETIME_FORMATTER.format(home.updatedAt()) : null;

        TemplateInstance view = index.data("home", home)
                .data("metaTitle", metaTitle)
                .data("metaDescription", metaDescription)
                .data("updatedAt", updatedAt);

        return Response.ok(view).build();
    }
}
