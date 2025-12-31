package it.me.web.dto.response;

import it.me.web.dto.request.FoodCostRequest;
import lombok.Builder;

@Builder
public record FoodCostResponse(FoodCostRequest foodCostRequest, Double resourceCost, Double targetCost) {}
