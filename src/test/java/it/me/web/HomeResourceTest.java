//package it.me.web;
//
//import io.quarkus.qute.Template;
//import io.quarkus.qute.TemplateInstance;
//import it.me.repository.entity.PageContentEntity;
//import it.me.repository.page.content.PageContentReadBySlugRepositoryJpa;
//import it.me.web.view.HomeResource;
//import jakarta.ws.rs.NotFoundException;
//import jakarta.ws.rs.core.Response;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.ZoneId;
//import java.time.ZonedDateTime;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class HomeResourceTest {
//
//    @InjectMocks
//    private HomeResource sut;
//
//    @Mock
//    Template templateIndex;
//
//    @Mock
//    PageContentReadBySlugRepositoryJpa pageContentReadBySlugRepositoryJpa;
//
//    @Test
//    void home_notFound() {
//        // given
//        given(pageContentReadBySlugRepositoryJpa.readBySlug(eq("home")))
//                .willReturn(Optional.empty());
//
//        //when
//        NotFoundException nfe = assertThrows(NotFoundException.class, () -> sut.home());
//
//        // then
//        assertThat(nfe).isInstanceOf(NotFoundException.class);
//        assertThat(nfe.getMessage()).contains("not found");
//    }
//
//    @Test
//    void writeMetaDescription() {
//        // given
//        var pageContent = new PageContentEntity()
//                .setSlug("home")
//                .setTitle("title")
//                .setBody("hi           guysss   ")
//                .setUpdatedAt(ZonedDateTime.of(2025, 1, 2, 3, 4, 5, 0, ZoneId.of("Europe/Paris")));
//        given(pageContentReadBySlugRepositoryJpa.readBySlug(eq("home")))
//                .willReturn(Optional.of(pageContent));
//        var templateInstance = Mockito.mock(TemplateInstance.class, RETURNS_SELF);
//        given(templateIndex.data(anyString(), any())).willReturn(templateInstance);
//
//        //when
//        Response result = sut.home();
//
//        //then
//        assertThat(result.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
//
//        ArgumentCaptor<String> metaTitle = ArgumentCaptor.forClass(String.class);
//        ArgumentCaptor<String> metaDescription = ArgumentCaptor.forClass(String.class);
//        ArgumentCaptor<String> updatedAt = ArgumentCaptor.forClass(String.class);
//
//        // verify chaining
//        verify(templateIndex).data(eq("home"), same(pageContent));
//        verify(templateInstance).data(eq("metaTitle"), metaTitle.capture());
//        verify(templateInstance).data(eq("metaDescription"), metaDescription.capture());
//        verify(templateInstance).data(eq("updatedAt"), updatedAt.capture());
//
//        assertThat(metaTitle.getValue()).isEqualTo("title");
//        assertThat(metaDescription.getValue()).isEqualTo("hi guysss");
//        assertThat(updatedAt.getValue()).isEqualTo("2025-01-02 03:04:05");
//
//        verifyNoMoreInteractions(templateIndex, templateInstance, pageContentReadBySlugRepositoryJpa);
//    }
//
//    @Test
//    void writeMetaDescriptionTruncated_andUpdatedAtNull() {
//        // given
//        var body = "x".repeat(200);
//        var pageContent = new PageContentEntity()
//                .setSlug("home")
//                .setTitle("title")
//                .setBody(body)
//                .setUpdatedAt(null);
//        given(pageContentReadBySlugRepositoryJpa.readBySlug(eq("home")))
//                .willReturn(Optional.of(pageContent));
//        var templateInstance = Mockito.mock(TemplateInstance.class, RETURNS_SELF);
//        given(templateIndex.data(anyString(), any())).willReturn(templateInstance);
//
//        //when
//        Response result = sut.home();
//
//        //then
//        assertThat(result.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
//
//        ArgumentCaptor<String> metaTitle = ArgumentCaptor.forClass(String.class);
//        ArgumentCaptor<String> metaDescription = ArgumentCaptor.forClass(String.class);
//        ArgumentCaptor<String> updatedAt = ArgumentCaptor.forClass(String.class);
//
//        verify(templateIndex).data(eq("home"), same(pageContent));
//        verify(templateInstance).data(eq("metaTitle"), metaTitle.capture());
//        verify(templateInstance).data(eq("metaDescription"), metaDescription.capture());
//        verify(templateInstance).data(eq("updatedAt"), updatedAt.capture());
//
//        var value = metaDescription.getValue();
//        assertThat(value.length()).isEqualTo(160);
//        assertThat(value).endsWith("...");
//        assertThat(updatedAt.getValue()).isNull();
//    }
//
//    @Test
//    void write_bodyBlank_andMetaDescriptionBlank_andUpdatedAtNull() {
//        // given
//        var pageContent = new PageContentEntity()
//                .setSlug("home")
//                .setTitle("title")
//                .setBody("")
//                .setUpdatedAt(null);
//        given(pageContentReadBySlugRepositoryJpa.readBySlug(eq("home")))
//                .willReturn(Optional.of(pageContent));
//        var templateInstance = Mockito.mock(TemplateInstance.class, RETURNS_SELF);
//        given(templateIndex.data(anyString(), any())).willReturn(templateInstance);
//
//        //when
//        Response result = sut.home();
//
//        //then
//        assertThat(result.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
//
//        ArgumentCaptor<String> metaTitle = ArgumentCaptor.forClass(String.class);
//        ArgumentCaptor<String> metaDescription = ArgumentCaptor.forClass(String.class);
//        ArgumentCaptor<String> updatedAt = ArgumentCaptor.forClass(String.class);
//
//        verify(templateIndex).data(eq("home"), same(pageContent));
//        verify(templateInstance).data(eq("metaTitle"), metaTitle.capture());
//        verify(templateInstance).data(eq("metaDescription"), metaDescription.capture());
//        verify(templateInstance).data(eq("updatedAt"), updatedAt.capture());
//
//        assertThat(updatedAt.getValue()).isNull();
//        assertThat(metaDescription.getValue()).isBlank();
//    }
//
//    @Test
//    void write_bodyNull_andMetaDescriptionNull_andUpdatedAtNull() {
//        // given
//        var pageContent = new PageContentEntity()
//                .setSlug("home")
//                .setTitle("title")
//                .setBody(null)
//                .setUpdatedAt(null);
//        given(pageContentReadBySlugRepositoryJpa.readBySlug(eq("home")))
//                .willReturn(Optional.of(pageContent));
//        var templateInstance = Mockito.mock(TemplateInstance.class, RETURNS_SELF);
//        given(templateIndex.data(anyString(), any())).willReturn(templateInstance);
//
//        //when
//        Response result = sut.home();
//
//        //then
//        assertThat(result.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
//
//        ArgumentCaptor<String> metaTitle = ArgumentCaptor.forClass(String.class);
//        ArgumentCaptor<String> metaDescription = ArgumentCaptor.forClass(String.class);
//        ArgumentCaptor<String> updatedAt = ArgumentCaptor.forClass(String.class);
//
//        verify(templateIndex).data(eq("home"), same(pageContent));
//        verify(templateInstance).data(eq("metaTitle"), metaTitle.capture());
//        verify(templateInstance).data(eq("metaDescription"), metaDescription.capture());
//        verify(templateInstance).data(eq("updatedAt"), updatedAt.capture());
//
//        assertThat(pageContent.body()).isNull();
//        assertThat(updatedAt.getValue()).isNull();
//        assertThat(metaDescription.getValue()).isBlank();
//    }
//}