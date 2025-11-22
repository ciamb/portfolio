package it.me.domain.service;

import it.me.entity.PageContent;
import it.me.repository.PageContentReadBySlugRepository;
import it.me.web.dto.request.PageContentUpdateRequest;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class PageContentUpdateBySlugServiceTest {

    @InjectMocks
    private PageContentUpdateBySlugService sut;

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
    void updatePageContentBySlug_titleShouldThrowIllegalArgumentException() {
        //given
        var pageContent = new PageContent();
        var title = "t".repeat(140);
        var pageContentUpdateRequest = new PageContentUpdateRequest(title, null, "");
        given(pageContentReadBySlugRepository.readBySlug(anyString()))
                .willReturn(Optional.of(pageContent));

        //when
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> sut.updatePageContentBySlug("any", pageContentUpdateRequest));

        //then
        assertThat(iae.getMessage()).contains("title is longer than 120");
    }

    @Test
    void updatePageContentBySlug_subtitleShouldThrowIllegalArgumentException() {
        //given
        var pageContent = new PageContent();
        var subtitle = "s".repeat(300);
        var pageContentUpdateRequest = new PageContentUpdateRequest("", subtitle, null);
        given(pageContentReadBySlugRepository.readBySlug(anyString()))
                .willReturn(Optional.of(pageContent));

        //when
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> sut.updatePageContentBySlug("any", pageContentUpdateRequest));

        //then
        assertThat(iae.getMessage()).contains("subtitle is longer than 240");
    }

    @Test
    void updatePageContentBySlug_shouldUpdatePageContent() {
        //given
        var pageContent = new PageContent();
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