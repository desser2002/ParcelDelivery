package org.dzianisbova.parceldelivery.packing.config;

import org.dzianisbova.parceldelivery.packing.domain.algorithm.BottomUpLevelAlgorithm;
import org.dzianisbova.parceldelivery.packing.domain.algorithm.TopDownLevelAlgorithm;
import org.dzianisbova.parceldelivery.packing.domain.algorithm.sort.BaseAreaDescSorter;
import org.dzianisbova.parceldelivery.packing.domain.policy.PackingPolicy;
import org.dzianisbova.parceldelivery.packing.domain.service.FragileZonePackingStrategy;
import org.dzianisbova.parceldelivery.packing.domain.service.MultiVehiclePackingService;
import org.dzianisbova.parceldelivery.packing.domain.service.PackingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class PackingConfiguration {
    @Bean
    public PackingStrategy packingStrategy(List<PackingPolicy> policies) {
        return new FragileZonePackingStrategy(
                new BottomUpLevelAlgorithm(),
                new TopDownLevelAlgorithm(),
                new BaseAreaDescSorter(),
                policies
        );
    }

    @Bean
    public MultiVehiclePackingService multiVehiclePackingService(PackingStrategy strategy) {
        return new MultiVehiclePackingService(strategy);
    }
}