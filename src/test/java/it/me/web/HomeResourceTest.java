package it.me.web;

import static it.me.domain.PortfolioPublicConst.GITHUB_CIAMB_PORTFOLIO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import it.me.domain.dto.PageContent;
import it.me.domain.repository.cv.file.CvFileExistsIsActiveRepository;
import it.me.domain.repository.page.content.PageContentReadBySlugRepository;
import it.me.web.view.HomeResource;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HomeResourceTest {

    @InjectMocks
    private HomeResource sut;

    @Mock
    Template templateIndex;

    @Mock
    PageContentReadBySlugRepository pageContentReadBySlugRepository;

    @Mock
    CvFileExistsIsActiveRepository cvFileExistsIsActiveRepository;

    @Test
    void home_notFound() {
        // given
        given(pageContentReadBySlugRepository.readBySlug(eq("home"))).willReturn(Optional.empty());

        // when
        NotFoundException nfe = assertThrows(NotFoundException.class, () -> sut.home());

        // then
        assertThat(nfe).isInstanceOf(NotFoundException.class);
        assertThat(nfe.getMessage()).contains("not found");
    }

    @Test
    void writeMetaDescription() {
        // given
        var pageContent = PageContent.builder()
                .slug("home")
                .title("title")
                .body("hi           guysss   ")
                .updatedAt(ZonedDateTime.of(2025, 1, 2, 3, 4, 5, 0, ZoneId.of("Europe/Paris")))
                .build();
        given(pageContentReadBySlugRepository.readBySlug(eq("home"))).willReturn(Optional.of(pageContent));
        given(cvFileExistsIsActiveRepository.existsActiveCvFile()).willReturn(Boolean.TRUE);
        var templateInstance = Mockito.mock(TemplateInstance.class, RETURNS_SELF);
        given(templateIndex.data(anyString(), any())).willReturn(templateInstance);

        // when
        Response result = sut.home();

        // then
        assertThat(result.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());

        ArgumentCaptor<String> metaTitle = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> metaDescription = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> updatedAt = ArgumentCaptor.forClass(String.class);

        // verify chaining
        verify(templateIndex).data(eq("home"), same(pageContent));
        verify(templateInstance).data(eq("metaTitle"), metaTitle.capture());
        verify(templateInstance).data(eq("metaDescription"), metaDescription.capture());
        verify(templateInstance).data(eq("updatedAt"), updatedAt.capture());
        verify(templateInstance).data(eq("isCvFilePresent"), eq(Boolean.TRUE));
        verify(templateInstance).data(eq("githubPage"), eq(GITHUB_CIAMB_PORTFOLIO));

        assertThat(metaTitle.getValue()).isEqualTo("title");
        assertThat(metaDescription.getValue()).isEqualTo("hi guysss");
        assertThat(updatedAt.getValue()).isEqualTo("2025-01-02 03:04:05");

        verifyNoMoreInteractions(
                templateIndex, templateInstance, pageContentReadBySlugRepository, cvFileExistsIsActiveRepository);
    }

    @Test
    void writeMetaDescriptionTruncated_andUpdatedAtNull() {
        // given
        var body = "x".repeat(200);
        var pageContent = PageContent.builder()
                .slug("home")
                .title("title")
                .body(body)
                .updatedAt(null)
                .build();
        given(pageContentReadBySlugRepository.readBySlug(eq("home"))).willReturn(Optional.of(pageContent));
        given(cvFileExistsIsActiveRepository.existsActiveCvFile()).willReturn(Boolean.TRUE);
        var templateInstance = Mockito.mock(TemplateInstance.class, RETURNS_SELF);
        given(templateIndex.data(anyString(), any())).willReturn(templateInstance);

        // when
        Response result = sut.home();

        // then
        assertThat(result.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());

        ArgumentCaptor<String> metaTitle = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> metaDescription = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> updatedAt = ArgumentCaptor.forClass(String.class);

        verify(templateIndex).data(eq("home"), same(pageContent));
        verify(templateInstance).data(eq("metaTitle"), metaTitle.capture());
        verify(templateInstance).data(eq("metaDescription"), metaDescription.capture());
        verify(templateInstance).data(eq("updatedAt"), updatedAt.capture());

        var value = metaDescription.getValue();
        assertThat(value.length()).isEqualTo(160);
        assertThat(value).endsWith("...");
        assertThat(updatedAt.getValue()).isNull();
    }

    @Test
    void write_bodyBlank_andMetaDescriptionBlank_andUpdatedAtNull() {
        // given
        var pageContent = PageContent.builder()
                .slug("home")
                .title("title")
                .body("")
                .updatedAt(null)
                .build();
        given(pageContentReadBySlugRepository.readBySlug(eq("home"))).willReturn(Optional.of(pageContent));
        given(cvFileExistsIsActiveRepository.existsActiveCvFile()).willReturn(Boolean.TRUE);
        var templateInstance = Mockito.mock(TemplateInstance.class, RETURNS_SELF);
        given(templateIndex.data(anyString(), any())).willReturn(templateInstance);

        // when
        Response result = sut.home();

        // then
        assertThat(result.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());

        ArgumentCaptor<String> metaTitle = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> metaDescription = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> updatedAt = ArgumentCaptor.forClass(String.class);

        verify(templateIndex).data(eq("home"), same(pageContent));
        verify(templateInstance).data(eq("metaTitle"), metaTitle.capture());
        verify(templateInstance).data(eq("metaDescription"), metaDescription.capture());
        verify(templateInstance).data(eq("updatedAt"), updatedAt.capture());

        assertThat(updatedAt.getValue()).isNull();
        assertThat(metaDescription.getValue()).isBlank();
    }

    @Test
    void write_bodyNull_andMetaDescriptionNull_andUpdatedAtNull() {
        // given
        var pageContent = PageContent.builder()
                .slug("home")
                .title("title")
                .body(null)
                .updatedAt(null)
                .build();
        given(pageContentReadBySlugRepository.readBySlug(eq("home"))).willReturn(Optional.of(pageContent));
        given(cvFileExistsIsActiveRepository.existsActiveCvFile()).willReturn(Boolean.TRUE);

        var templateInstance = Mockito.mock(TemplateInstance.class, RETURNS_SELF);
        given(templateIndex.data(anyString(), any())).willReturn(templateInstance);

        // when
        Response result = sut.home();

        // then
        assertThat(result.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());

        ArgumentCaptor<String> metaTitle = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> metaDescription = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> updatedAt = ArgumentCaptor.forClass(String.class);

        verify(templateIndex).data(eq("home"), same(pageContent));
        verify(templateInstance).data(eq("metaTitle"), metaTitle.capture());
        verify(templateInstance).data(eq("metaDescription"), metaDescription.capture());
        verify(templateInstance).data(eq("updatedAt"), updatedAt.capture());

        assertThat(pageContent.body()).isNull();
        assertThat(updatedAt.getValue()).isNull();
        assertThat(metaDescription.getValue()).isBlank();
    }
}
