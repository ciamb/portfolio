package it.me.web.api.ingredient.unit;

import it.me.domain.service.ingredient.unit.IngredientUnitListService;
import it.me.web.dto.response.IngredientUnitListResponse;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/api")
public class IngredientUnitListResource {

    @Inject
    IngredientUnitListService ingredientUnitListService;

    @Path("/ingredient/unit-list")
    @GET
    public Response getIngredientUnitList() {
        List<IngredientUnitListResponse> response = ingredientUnitListService.ingredientUnitList();
        return Response.ok(response).build();
    }
}
