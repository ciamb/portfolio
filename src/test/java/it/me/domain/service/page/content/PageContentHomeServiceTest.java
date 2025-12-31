package it.me.domain.service.page.content;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;

import it.me.domain.Page;
import it.me.domain.dto.PageContent;
import it.me.domain.repository.page.content.PageContentPersistRepository;
import it.me.domain.repository.page.content.PageContentReadBySlugRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class})
class PageContentHomeServiceTest {

    @InjectMocks
    private PageContentHomeService sut;

    @Mock
    PageContentPersistRepository pageContentPersistRepository;

    @Mock
    PageContentReadBySlugRepository pageContentReadBySlugRepositoryJpa;

    @Test
    @DisplayName("1. Should return the home page from database")
    void createHomeIfMissing_returnsHome_whenIsPresent() {
        // given
        var pageContent = PageContent.builder().build();
        given(pageContentReadBySlugRepositoryJpa.readBySlug(ArgumentMatchers.eq(Page.HOME.getSlug())))
                .willReturn(Optional.of(pageContent));

        // when
        PageContent result = sut.createHomeIfMissing();

        // then
        assertThat(result).isSameAs(pageContent);
        var inOrder = Mockito.inOrder(pageContentReadBySlugRepositoryJpa);
        inOrder.verify(pageContentReadBySlugRepositoryJpa, times(1)).readBySlug(eq(Page.HOME.getSlug()));
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("2. Should return the default home page")
    void createHomeIfMissing_returnsDefaultHome_whenIsNotPresent() {
        // given
        given(pageContentReadBySlugRepositoryJpa.readBySlug(eq(Page.HOME.getSlug())))
                .willReturn(Optional.empty());
        ArgumentCaptor<PageContent> pageContent = ArgumentCaptor.forClass(PageContent.class);

        // when
        PageContent result = sut.createHomeIfMissing();

        // then
        var inOrder = Mockito.inOrder(pageContentReadBySlugRepositoryJpa, pageContentPersistRepository);
        inOrder.verify(pageContentReadBySlugRepositoryJpa, times(1)).readBySlug(eq(Page.HOME.getSlug()));
        inOrder.verify(pageContentPersistRepository, times(1)).persist(pageContent.capture());
        inOrder.verifyNoMoreInteractions();

        PageContent persisted = pageContent.getValue();
        assertSame(result, persisted);
        assertThat(persisted.title()).contains("Benvenuto sul portfolio del tuo Ciambellino preferito");
    }
}
