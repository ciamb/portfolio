package it.me.web.view;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import it.me.domain.repository.cv.file.CvFileExistsIsActiveRepository;
import it.me.domain.repository.page.content.PageContentReadBySlugRepository;
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

    @Inject
    Template home;

    @Inject
    PageContentReadBySlugRepository pageContentReadBySlugRepository;

    @Inject
    CvFileExistsIsActiveRepository cvFileExistsIsActiveRepository;

    @GET
    public Response home() {
        var pageContent = pageContentReadBySlugRepository
                .readBySlug("home")
                .orElseThrow(() -> new NotFoundException("Page slug=%s not found".formatted("home")));

        var metaTitle = pageContent.title();

        var body = pageContent.body();
        var metaDescription = "";
        if (body != null && !body.isBlank()) {
            metaDescription = body.replaceAll("\\s+", " ").strip();
            if (metaDescription.length() > 160) {
                metaDescription = metaDescription.substring(0, 157).concat("...");
            }
        }

        var updatedAt = pageContent.updatedAt() != null ? IT_DATETIME_FORMATTER.format(pageContent.updatedAt()) : null;

        boolean isCvFilePresent = cvFileExistsIsActiveRepository.existsActiveCvFile();

        TemplateInstance view = home.data("home", pageContent)
                .data("metaTitle", metaTitle)
                .data("metaDescription", metaDescription)
                .data("updatedAt", updatedAt)
                .data("isCvFilePresent", isCvFilePresent);

        return Response.ok(view).build();
    }
}
