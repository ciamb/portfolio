package it.me.domain.service.page.content;

import it.me.domain.dto.PageContent;
import it.me.domain.repository.page.content.PageContentReadBySlugRepository;
import it.me.domain.repository.page.content.PageContentUpdateBySlugRepository;
import it.me.web.dto.request.PageContentUpdateRequest;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class PageContentUpdateBySlugServiceTest {

    @InjectMocks
    private PageContentUpdateBySlugService sut;

    @Mock
    PageContentUpdateBySlugRepository pageContentUpdateBySlugRepository;

    @Mock
    PageContentReadBySlugRepository pageContentReadBySlugRepository;

    @Test
    void updatePageContentBySlug_shouldThrowNotFoundException() {
        //given
        var pageContentUpdateRequest = mock(PageContentUpdateRequest.class);
        given(pageContentReadBySlugRepository.readBySlug(anyString()))
                .willReturn(Optional.empty());

        //when
        NotFoundException nfe = assertThrows(NotFoundException.class,
                () -> sut.updatePageContentBySlug("any", pageContentUpdateRequest));

        //then
        assertThat(nfe.getMessage()).contains("Page not found");
    }

    @Test
    void updatePageContentBySlug_shouldUpdatePageContent() {
        //given
        var pageContent = PageContent.builder().build();
        var title = "t".repeat(20);
        var subtitle = "s".repeat(130);
        var pageContentUpdateRequest = new PageContentUpdateRequest(title, subtitle, "hello");
        given(pageContentReadBySlugRepository.readBySlug(anyString()))
                .willReturn(Optional.of(pageContent));

        //when
        PageContent result = assertDoesNotThrow(() -> sut.updatePageContentBySlug("any", pageContentUpdateRequest));

        //then
        assertThat(result.title()).isSameAs(title);
        assertThat(result.subtitle()).isSameAs(subtitle);
        assertThat(result.body()).isSameAs("hello");
    }
}