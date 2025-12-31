package it.me.web.view;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/food-cost-calculator")
public class FoodCostCalculatorResource {

    @Inject
    Template foodCostCalculator;

    @GET
    public Response foodCostCalculator() {
        TemplateInstance view = foodCostCalculator.instance();
        return Response.ok(view).build();
    }
}
