package org.dzianisbova.parceldelivery.loadgenerator.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
class ShipmentKafkaConsumer {
    private final RestClient restClient;

    @KafkaListener(topics = "create-shipment-requests")
    void consume(CreateShipmentMessage message) {
        restClient.post()
                .uri("/shipment")
                .body(message)
                .retrieve()
                .toBodilessEntity();
    }
}
