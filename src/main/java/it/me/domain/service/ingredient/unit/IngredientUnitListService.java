package it.me.domain.service.ingredient.unit;

import it.me.domain.dto.Ingredient;
import it.me.web.dto.response.IngredientUnitListResponse;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class IngredientUnitListService {

    public List<IngredientUnitListResponse> ingredientUnitList() {
        List<Ingredient.Unit> aviableUnitList = Ingredient.Unit.getAviableUnitList();
        List<IngredientUnitListResponse> responseList = new ArrayList<>();

        aviableUnitList.forEach((unit) -> {
            IngredientUnitListResponse tempUnit = IngredientUnitListResponse.builder()
                    .value(unit)
                    .symbol(unit.symbol())
                    .build();
            responseList.add(tempUnit);
        });

        return responseList;
    }
}
