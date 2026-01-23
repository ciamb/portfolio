package it.me.repository.page.content.mapper;

import static org.junit.jupiter.api.Assertions.*;

import it.me.domain.dto.PageContent;
import it.me.repository.entity.PageContentEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PageContentEntity2PageContentMapperTest {

    @InjectMocks
    private PageContentEntity2PageContentMapper sut;

    @Test
    void shouldApply() {
        // given
        PageContentEntity pce = new PageContentEntity();
        pce.setId(1L);
        // when
        PageContent apply = sut.apply(pce);
        // then
        assertNotNull(apply);
        assertEquals(pce.id(), apply.id());
    }

    @Test
    void shouldReturnNull() {
        // given
        // when
        // then
        assertNull(sut.apply(null));
    }
}
