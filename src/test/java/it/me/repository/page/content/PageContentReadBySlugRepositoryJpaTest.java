package it.me.repository.page.content;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;

import it.me.domain.dto.PageContent;
import it.me.repository.entity.PageContentEntity;
import it.me.repository.page.content.mapper.PageContentEntity2PageContentMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PageContentReadBySlugRepositoryJpaTest {

    @InjectMocks
    private PageContentReadBySlugRepositoryJpa sut;

    @Mock
    EntityManager em;

    @Mock
    PageContentEntity2PageContentMapper pageContentEntity2PageContentMapper;

    @Mock
    TypedQuery<PageContentEntity> query;

    @Test
    @DisplayName("Do the query and return an optional with the result present")
    void readBySlug_returnsFirst_whenSingleResult() {
        // given
        var slug = "slug";
        var entity = new PageContentEntity();
        var pageContent = PageContent.builder().slug(slug).build();

        given(em.createNamedQuery(PageContentEntity.READ_BY_SLUG, PageContentEntity.class))
                .willReturn(query);
        given(query.setParameter(eq("slug"), eq(slug))).willReturn(query);
        given(query.getResultStream()).willReturn(Stream.of(entity));
        given(pageContentEntity2PageContentMapper.apply(entity)).willReturn(pageContent);

        // when
        Optional<PageContent> result = assertDoesNotThrow(() -> sut.readBySlug(slug));

        // then
        assertThat(result).isPresent().contains(pageContent);
        var inOrder = Mockito.inOrder(em, query);
        inOrder.verify(em, times(1)).createNamedQuery(PageContentEntity.READ_BY_SLUG, PageContentEntity.class);
        inOrder.verify(query, times(1)).setParameter(eq("slug"), eq(slug));
        inOrder.verify(query, times(1)).getResultStream();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("Do the query and return an empty optional")
    void readBySlug_returnsEmpty_whenNoResults() {
        // given
        var slug = "slug";

        given(em.createNamedQuery(PageContentEntity.READ_BY_SLUG, PageContentEntity.class))
                .willReturn(query);
        given(query.setParameter(eq("slug"), eq(slug))).willReturn(query);
        given(query.getResultStream()).willReturn(Stream.empty());

        // when
        Optional<PageContent> result = assertDoesNotThrow(() -> sut.readBySlug(slug));

        // then
        assertThat(result).isEmpty();
        var inOrder = Mockito.inOrder(em, query);
        inOrder.verify(em, times(1)).createNamedQuery(PageContentEntity.READ_BY_SLUG, PageContentEntity.class);
        inOrder.verify(query, times(1)).setParameter(eq("slug"), eq(slug));
        inOrder.verify(query, times(1)).getResultStream();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("Do the query and return an optional with the first result present if more are present")
    void readBySlug_returnsFirst_whenMultipleResults() {
        // given
        var firstPage = new PageContentEntity();
        var secondPage = new PageContentEntity();

        given(em.createNamedQuery(PageContentEntity.READ_BY_SLUG, PageContentEntity.class))
                .willReturn(query);
        given(query.setParameter(eq("slug"), eq("first"))).willReturn(query);
        given(query.getResultStream()).willReturn(Stream.of(firstPage, secondPage));
        PageContent first = PageContent.builder().slug("first").build();
        given(pageContentEntity2PageContentMapper.apply(firstPage)).willReturn(first);

        // when
        Optional<PageContent> out = sut.readBySlug("first");

        // then
        assertThat(out).isPresent().contains(first);
        var inOrder = Mockito.inOrder(em, query, pageContentEntity2PageContentMapper);
        inOrder.verify(em, times(1)).createNamedQuery(PageContentEntity.READ_BY_SLUG, PageContentEntity.class);
        inOrder.verify(query, times(1)).setParameter(eq("slug"), eq("first"));
        inOrder.verify(query, times(1)).getResultStream();
        inOrder.verify(pageContentEntity2PageContentMapper, times(1)).apply(firstPage);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("Throw NPE when slug parameter is null")
    void readBySlug_throwsOnNull() {
        // given
        given(em.createNamedQuery(PageContentEntity.READ_BY_SLUG, PageContentEntity.class))
                .willReturn(query);

        // when
        // then
        assertThatThrownBy(() -> sut.readBySlug(null)).isInstanceOf(NullPointerException.class);
    }
}
