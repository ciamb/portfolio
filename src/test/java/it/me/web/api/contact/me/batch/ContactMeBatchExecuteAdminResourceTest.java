package it.me.web.api.contact.me.batch;

import it.me.domain.service.contact.me.batch.ContactMeBatchManagerService;
import it.me.web.dto.response.ContactMeBatchResponse;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ContactMeBatchExecuteAdminResourceTest {

    @InjectMocks
    private ContactMeBatchExecuteAdminResource sut;

    @Mock
    ContactMeBatchManagerService contactMeBatchManagerService;

    @Mock
    ContactMeBatchResponse contactMeBatchResponse;

    @Test
    void shouldCallExecuteBatch() {
        //given
        given(contactMeBatchManagerService.executeBatch())
                .willReturn(contactMeBatchResponse);
        // when
        Response response = sut.executeBatch();
        //then
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertNotNull(response.getEntity());

    }
}