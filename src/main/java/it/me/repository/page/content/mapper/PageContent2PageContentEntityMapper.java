package it.me.repository.page.content.mapper;

import it.me.domain.dto.PageContent;
import it.me.repository.entity.PageContentEntity;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

import java.util.function.Function;

@ApplicationScoped
public class PageContent2PageContentEntityMapper implements Function<PageContent, PageContentEntity> {
    private final Logger logger = Logger.getLogger(PageContent2PageContentEntityMapper.class);

    @Override
    public PageContentEntity apply(PageContent pageContent) {
        if (pageContent == null) {
            logger.warn("mapping null pageContent to entity");
            return null;
        }

        return new PageContentEntity()
                .setSlug(pageContent.slug())
                .setTitle(pageContent.title())
                .setSubtitle(pageContent.subtitle())
                .setBody(pageContent.body())
                .setUpdatedAt(pageContent.updatedAt());
    }
}
