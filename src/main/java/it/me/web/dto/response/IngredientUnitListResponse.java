package it.me.web.dto.response;

import it.me.domain.dto.Ingredient;
import lombok.Builder;

@Builder
public record IngredientUnitListResponse(Ingredient.Unit value, String symbol) {}
