package it.me.web.api;

import it.me.domain.service.ContactMeBatchConfigUpdateIsActiveService;
import it.me.web.dto.request.ContactMeBatchConfigUpdateRequest;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ContactMeBatchConfigUpdateAdminResourceTest {

    @InjectMocks
    private ContactMeBatchConfigUpdateAdminResource sut;

    @Mock
    ContactMeBatchConfigUpdateIsActiveService contactMeBatchConfigUpdateIsActiveService;
    @Mock
    ContactMeBatchConfigUpdateRequest contactMeBatchConfigUpdateRequest;

    @Test
    void shouldCallServiceAndResponseActive() {
        //given
        given(contactMeBatchConfigUpdateIsActiveService
                .updateContactMeBatchConfig(any(ContactMeBatchConfigUpdateRequest.class)))
                .willReturn(true);

        //when
        Response response = sut.updateContactMeBatchConfig(contactMeBatchConfigUpdateRequest);

        //then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getEntity()).isNotNull();
    }

    @Test
    void shouldCallServiceAndResponseNotActive() {
        //given
        given(contactMeBatchConfigUpdateIsActiveService
                .updateContactMeBatchConfig(any(ContactMeBatchConfigUpdateRequest.class)))
                .willReturn(false);

        //when
        Response response = sut.updateContactMeBatchConfig(contactMeBatchConfigUpdateRequest);

        //then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getEntity()).isNotNull();
    }
}