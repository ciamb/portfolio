package it.me.repository.page.content.mapper;

import static org.junit.jupiter.api.Assertions.*;

import it.me.domain.dto.PageContent;
import it.me.repository.entity.PageContentEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PageContent2PageContentEntityMapperTest {

    @InjectMocks
    private PageContent2PageContentEntityMapper sut;

    @Test
    void shouldApply() {
        // given
        PageContent pc = PageContent.builder().slug("ciao").build();
        // when
        PageContentEntity entity = sut.apply(pc);
        // then
        assertNotNull(entity);
        assertEquals(entity.slug(), pc.slug());
    }

    @Test
    void shouldReturnNull() {
        // given
        // when
        // then
        assertNull(sut.apply(null));
    }
}
