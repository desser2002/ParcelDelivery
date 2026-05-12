package org.dzianisbova.parceldelivery.validation.platenumber;

import jakarta.annotation.PostConstruct;
import org.dzianisbova.parceldelivery.validation.platenumber.strategys.PlateValidationStrategy;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
class PlateStrategyRegistry {
    private final Map<PlateRegion, PlateValidationStrategy> regionToStrategy;

    public PlateStrategyRegistry(List<PlateValidationStrategy> strategies) {
        regionToStrategy = strategies.stream()
                .collect(Collectors.toMap(PlateValidationStrategy::getRegion, Function.identity()));
    }

    @PostConstruct
    void verifyAllRegionsCovered() {
        var missing = EnumSet.allOf(PlateRegion.class);
        missing.removeAll(regionToStrategy.keySet());
        if (!missing.isEmpty()) {
            throw new IllegalStateException(
                    "Missing PlateValidationStrategy for regions: " + missing
            );
        }
    }

    public PlateValidationStrategy get(PlateRegion region) {
        return regionToStrategy.get(region);
    }
}