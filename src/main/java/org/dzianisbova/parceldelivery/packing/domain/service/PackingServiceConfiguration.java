package org.dzianisbova.parceldelivery.packing.domain.service;

import org.dzianisbova.parceldelivery.packing.domain.algorithm.PackingAlgorithm;
import org.dzianisbova.parceldelivery.packing.domain.algorithm.sort.ParcelSorter;
import org.dzianisbova.parceldelivery.packing.domain.policy.PackingPolicy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class PackingServiceConfiguration {
    @Bean
    public PackingStrategy packingStrategy(
            @Qualifier("standardParcelAlgorithm") PackingAlgorithm standardAlgorithm,
            @Qualifier("fragileParcelAlgorithm") PackingAlgorithm fragileAlgorithm,
            ParcelSorter sorter,
            List<PackingPolicy> policies) {
        return new FragileZonePackingStrategy(
                standardAlgorithm,
                fragileAlgorithm,
                sorter,
                policies
        );
    }

}
