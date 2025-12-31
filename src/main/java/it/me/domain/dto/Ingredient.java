package it.me.domain.dto;

import java.util.Arrays;
import java.util.List;
import lombok.Builder;

@Builder
public record Ingredient(String name, Integer quantity, Unit unit, Double pricePerKgEur) {
    public enum Unit {
        NUMBER("n."),
        GRAMS("g");

        private final String symbol;

        Unit(String symbol) {
            this.symbol = symbol;
        }

        public String symbol() {
            return symbol;
        }

        public static List<Ingredient.Unit> getAviableUnitList() {
            return Arrays.stream(Unit.values()).toList();
        }
    }
}
