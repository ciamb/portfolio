package it.me.repository;

import it.me.entity.PageContent;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class PageContentReadBySlugRepositoryTest {

    private PageContentReadBySlugRepository sut;

    @Mock
    EntityManager em;

    @Mock
    TypedQuery<PageContent> query;

    @BeforeEach
    void setUp() {
        sut = new PageContentReadBySlugRepository(em);
    }

    @Test
    @DisplayName("Do the query and return an optional with the result present")
    void readBySlug_returnsFirst_whenSingleResult() {
        // given
        var slug = "slug";
        var pageContent = new PageContent();

        given(em.createNamedQuery(PageContent.READ_BY_SLUG, PageContent.class))
                .willReturn(query);
        given(query.setParameter(eq("slug"), eq(slug))).willReturn(query);
        given(query.getResultStream()).willReturn(Stream.of(pageContent));

        // when
        Optional<PageContent> result = assertDoesNotThrow(() -> sut.readBySlug(slug));

        //then
        assertThat(result).isPresent().contains(pageContent);
        var inOrder = Mockito.inOrder(em, query);
        inOrder.verify(em, times(1))
                .createNamedQuery(PageContent.READ_BY_SLUG, PageContent.class);
        inOrder.verify(query, times(1)).setParameter(eq("slug"), eq(slug));
        inOrder.verify(query, times(1)).getResultStream();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("Do the query and return an empty optional")
    void readBySlug_returnsEmpty_whenNoResults() {
        // given
        var slug = "slug";

        given(em.createNamedQuery(PageContent.READ_BY_SLUG, PageContent.class))
                .willReturn(query);
        given(query.setParameter(eq("slug"), eq(slug))).willReturn(query);
        given(query.getResultStream()).willReturn(Stream.empty());

        // when
        Optional<PageContent> result = assertDoesNotThrow(() -> sut.readBySlug(slug));

        //then
        assertThat(result).isEmpty();
        var inOrder = Mockito.inOrder(em, query);
        inOrder.verify(em, times(1))
                .createNamedQuery(PageContent.READ_BY_SLUG, PageContent.class);
        inOrder.verify(query, times(1)).setParameter(eq("slug"), eq(slug));
        inOrder.verify(query, times(1)).getResultStream();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("Do the query and return an optional with the first result present if more are present")
    void readBySlug_returnsFirst_whenMultipleResults() {
        // given
        var firstPage = new PageContent();
        var secondPage = new PageContent();

        given(em.createNamedQuery(PageContent.READ_BY_SLUG, PageContent.class)).willReturn(query);
        given(query.setParameter(eq("slug"), eq("first"))).willReturn(query);
        given(query.getResultStream()).willReturn(Stream.of(firstPage, secondPage));

        // when
        Optional<PageContent> out = sut.readBySlug("first");

        // then
        assertThat(out).isPresent().contains(firstPage);
        var inOrder = Mockito.inOrder(em, query);
        inOrder.verify(em, times(1))
                .createNamedQuery(PageContent.READ_BY_SLUG, PageContent.class);
        inOrder.verify(query, times(1)).setParameter(eq("slug"), eq("first"));
        inOrder.verify(query, times(1)).getResultStream();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("Throw NPE when slug parameter is null")
    void readBySlug_throwsOnNull() {
        // given
        given(em.createNamedQuery(PageContent.READ_BY_SLUG, PageContent.class)).willReturn(query);

        // when
        // then
        assertThatThrownBy(() -> sut.readBySlug(null))
                .isInstanceOf(NullPointerException.class);
    }
}