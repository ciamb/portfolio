//package it.me.domain.service.page.content;
//
//import it.me.domain.Page;
//import it.me.repository.entity.PageContentEntity;
//import it.me.repository.page.content.PageContentReadBySlugRepositoryJpa;
//import jakarta.persistence.EntityManager;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.*;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.times;
//
//@ExtendWith({MockitoExtension.class})
//class PageContentHomeServiceTest {
//
//    @InjectMocks
//    private PageContentHomeService sut;
//
//    @Mock
//    EntityManager em;
//
//    @Mock
//    PageContentReadBySlugRepositoryJpa pageContentReadBySlugRepositoryJpa;
//
//    @Test
//    @DisplayName("1. Should return the home page from database")
//    void createHomeIfMissing_returnsHome_whenIsPresent() {
//        // given
//        var pageContent = new PageContentEntity();
//        given(pageContentReadBySlugRepositoryJpa.readBySlug(ArgumentMatchers.eq(Page.HOME.getSlug())))
//                .willReturn(Optional.of(pageContent));
//
//        //when
//        PageContentEntity result = sut.createHomeIfMissing();
//
//        //then
//        assertThat(result).isSameAs(pageContent);
//        var inOrder = Mockito.inOrder(pageContentReadBySlugRepositoryJpa);
//        inOrder.verify(pageContentReadBySlugRepositoryJpa, times(1))
//                .readBySlug(eq(Page.HOME.getSlug()));
//        inOrder.verifyNoMoreInteractions();
//    }
//
//    @Test
//    @DisplayName("2. Should return the default home page")
//    void createHomeIfMissing_returnsDefaultHome_whenIsNotPresent() {
//        //given
//        given(pageContentReadBySlugRepositoryJpa.readBySlug(eq(Page.HOME.getSlug())))
//                .willReturn(Optional.empty());
//        ArgumentCaptor<PageContentEntity> pageContent = ArgumentCaptor.forClass(PageContentEntity.class);
//
//        //when
//        PageContentEntity result = sut.createHomeIfMissing();
//
//        //then
//        var inOrder = Mockito.inOrder(pageContentReadBySlugRepositoryJpa, em);
//        inOrder.verify(pageContentReadBySlugRepositoryJpa, times(1))
//                .readBySlug(eq(Page.HOME.getSlug()));
//        inOrder.verify(em, times(1)).persist(pageContent.capture());
//        inOrder.verifyNoMoreInteractions();
//
//        PageContentEntity persisted = pageContent.getValue();
//        assertSame(result, persisted);
//        assertThat(persisted.title()).contains("Ciao, sono Andrea, ma per gli amici Ciamb!");
//    }
//}