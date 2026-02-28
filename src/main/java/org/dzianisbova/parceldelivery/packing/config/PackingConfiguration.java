package org.dzianisbova.parceldelivery.packing.config;

import org.dzianisbova.parceldelivery.packing.domain.service.MultiVehiclePackingService;
import org.dzianisbova.parceldelivery.packing.domain.service.PackingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PackingConfiguration {
    @Bean
    public MultiVehiclePackingService multiVehiclePackingService(PackingStrategy strategy) {
        return new MultiVehiclePackingService(strategy);
    }
}