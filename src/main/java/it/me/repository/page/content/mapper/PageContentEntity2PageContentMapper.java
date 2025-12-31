package it.me.repository.page.content.mapper;

import it.me.domain.dto.PageContent;
import it.me.repository.entity.PageContentEntity;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.function.Function;
import org.jboss.logging.Logger;

@ApplicationScoped
public class PageContentEntity2PageContentMapper implements Function<PageContentEntity, PageContent> {
    private final Logger logger = Logger.getLogger(PageContentEntity2PageContentMapper.class);

    @Override
    public PageContent apply(PageContentEntity entity) {
        if (entity == null) {
            logger.warn("mapping null pageContentEntity");
            return null;
        }

        return PageContent.builder()
                .id(entity.id())
                .slug(entity.slug())
                .title(entity.title())
                .subtitle(entity.subtitle())
                .body(entity.body())
                .updatedAt(entity.updatedAt())
                .build();
    }
}
