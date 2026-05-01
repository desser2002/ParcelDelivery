package org.dzianisbova.parceldelivery.loadgenerator.infrastructure.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@ConditionalOnProperty(name = "loadgenerator.enabled", havingValue = "true")
@RequiredArgsConstructor
class GetShipmentKafkaConsumer {
    private final RestClient restClient;

    @KafkaListener(topics = "get-shipment-requests", concurrency = "6",
            groupId = "get-shipment-group", containerFactory = "getShipmentContainerFactory")
    void consume(GetShipmentMessage message) {
        restClient.get()
                .uri("/shipments/{trackingNumber}", message.trackingNumber())
                .retrieve()
                .toBodilessEntity();
    }
}
