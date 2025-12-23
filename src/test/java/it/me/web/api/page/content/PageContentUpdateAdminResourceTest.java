package it.me.web.api.page.content;

import it.me.domain.dto.PageContent;
import it.me.domain.service.page.content.PageContentUpdateBySlugService;
import it.me.repository.entity.PageContentEntity;
import it.me.web.dto.request.PageContentUpdateRequest;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class PageContentUpdateAdminResourceTest {

    @InjectMocks
    private PageContentUpdateAdminResource sut;

    @Mock
    PageContentUpdateBySlugService pageContentUpdateBySlugService;

    @Mock
    PageContentUpdateRequest pageContentUpdateRequest;

    @Test
    void shouldCallPageContentUpdateBySlugService() {
        //given
        var pageContent = PageContent.builder().build();
        given(pageContentUpdateBySlugService.updatePageContentBySlug(anyString(), any()))
                .willReturn(pageContent);

        //when
        Response result = sut.updatePageContentBySlug("slug", pageContentUpdateRequest);

        //then
        assertEquals(200, result.getStatus());
        assertEquals(pageContent, result.getEntity());
        Mockito.verify(pageContentUpdateBySlugService, times(1))
                .updatePageContentBySlug(anyString(), any());
        verifyNoMoreInteractions(pageContentUpdateBySlugService);
    }
}