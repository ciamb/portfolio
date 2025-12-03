package it.me.domain.mapper;

import it.me.domain.dto.ProcessedContactMe;
import it.me.entity.ContactMe;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith({MockitoExtension.class})
class ProcessedContactMeListMapperTest {

    @InjectMocks
    private ProcessedContactMeListMapper sut;

    @Mock
    ProcessedContactMeMapper processedContactMeMapper;

    @Mock
    ProcessedContactMe processedContactMe;

    @Test
    void shouldMapList() {
        // given
        given(processedContactMeMapper.apply(any(ContactMe.class)))
                .willReturn(processedContactMe);

        //when
        List<ProcessedContactMe> apply = sut.apply(List.of(new ContactMe()));

        //then
        assertNotNull(apply);
        assertThat(apply).hasSize(1);
    }

    @Test
    void shouldReturnEmptyApply() {
        // given
        //when
        List<ProcessedContactMe> apply = sut.apply(null);

        //then
        assertNotNull(apply);
        assertThat(apply).hasSize(0);
    }

}