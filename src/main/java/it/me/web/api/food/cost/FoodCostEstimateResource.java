package it.me.web.api.food.cost;

import it.me.domain.service.food.cost.FoodCostEstimateService;
import it.me.web.dto.request.FoodCostRequest;
import it.me.web.dto.response.FoodCostResponse;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

@Path("/api")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FoodCostEstimateResource {
    private final Logger logger = Logger.getLogger(FoodCostEstimateResource.class);

    @Inject
    FoodCostEstimateService foodCostEstimateService;

    @POST
    @Path("/food-cost/estimate")
    public Response estimatePrice(@Valid FoodCostRequest foodCostRequest) {
        logger.info("Received request /api/price/estimate");
        FoodCostResponse response = foodCostEstimateService.estimateFoodCost(foodCostRequest);
        return Response.ok(response).build();
    }
}
