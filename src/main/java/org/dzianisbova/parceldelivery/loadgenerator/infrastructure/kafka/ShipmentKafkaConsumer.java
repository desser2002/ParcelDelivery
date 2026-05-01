package org.dzianisbova.parceldelivery.loadgenerator.infrastructure.kafka;

import lombok.RequiredArgsConstructor;
import org.dzianisbova.parceldelivery.loadgenerator.application.TrackingNumberRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@ConditionalOnProperty(name = "loadgenerator.enabled", havingValue = "true")
@RequiredArgsConstructor
class ShipmentKafkaConsumer {
    private final RestClient restClient;
    private final TrackingNumberRegistry trackingNumberRegistry;

    @KafkaListener(topics = "create-shipment-requests", concurrency = "2",
            groupId = "create-shipment-group", containerFactory = "createShipmentContainerFactory")
    void consume(CreateShipmentMessage message) {
        ShipmentCreatedResponse response = restClient.post()
                .uri("/shipments")
                .body(message)
                .retrieve()
                .body(ShipmentCreatedResponse.class);

        if (response != null) {
            trackingNumberRegistry.register(response.trackingNumber());
        }
    }
}
