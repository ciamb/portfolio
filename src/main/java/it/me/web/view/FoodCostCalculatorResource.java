package it.me.web.view;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Path("/food-cost-calculator")
public class FoodCostCalculatorResource {

    @Inject
    Template foodCostCalculator;

    @ConfigProperty(name = "quarkus.application.version")
    String appVersion;

    @GET
    public Response foodCostCalculator() {
        TemplateInstance view = foodCostCalculator.data("appVersion", appVersion);
        return Response.ok(view).header("Cache-Control", "no-cache").build();
    }
}
