package it.me.web.api.food.cost;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import it.me.domain.service.food.cost.FoodCostEstimateService;
import it.me.web.dto.request.FoodCostRequest;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FoodCostEstimateResourceTest {

    @Mock
    private FoodCostEstimateService foodCostEstimateService;

    @InjectMocks
    private FoodCostEstimateResource sut;

    @Mock
    FoodCostRequest foodCostRequest;

    @Test
    void shouldCallServices() {
        // given
        // when
        Response res = sut.estimatePrice(foodCostRequest);

        // then
        assertEquals(Response.Status.OK.getStatusCode(), res.getStatus());
        verify(foodCostEstimateService).estimateFoodCost(foodCostRequest);
        verifyNoMoreInteractions(foodCostEstimateService);
    }
}
