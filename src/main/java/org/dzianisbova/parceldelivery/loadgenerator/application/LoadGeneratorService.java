package org.dzianisbova.parceldelivery.loadgenerator.application;

import lombok.RequiredArgsConstructor;
import org.dzianisbova.parceldelivery.loadgenerator.infrastructure.kafka.CreateShipmentMessage;
import org.dzianisbova.parceldelivery.loadgenerator.infrastructure.kafka.GetShipmentMessage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
@ConditionalOnProperty(name = "loadgenerator.enabled", havingValue = "true")
@RequiredArgsConstructor
public class LoadGeneratorService {
    private static final String CREATE_TOPIC = "create-shipment-requests";
    private static final String GET_TOPIC = "get-shipment-requests";
    private static final String[] CITIES = {"Warsaw", "Berlin", "Paris", "Minsk", "Prague"};
    private static final String[] COUNTRIES = {"PL", "DE", "FR", "BY", "CZ"};
    private static final String[] PRIORITIES = {"STANDARD", "EXPRESS"};

    private final KafkaTemplate<Object, Object> kafkaTemplate;
    private final TrackingNumberRegistry trackingNumberRegistry;

    public void generate(int count) {
        ThreadLocalRandom rng = ThreadLocalRandom.current();
        for (int i = 0; i < count; i++) {
            kafkaTemplate.send(CREATE_TOPIC, buildMessage(rng));
        }
    }

    public void generateTrackRequests(int count) {
        for (int i = 0; i < count; i++) {
            String trackingNumber = trackingNumberRegistry.isEmpty()
                    ? "INVALID-" + i
                    : trackingNumberRegistry.poll();
            kafkaTemplate.send(GET_TOPIC, new GetShipmentMessage(trackingNumber));
        }
    }

    private CreateShipmentMessage buildMessage(ThreadLocalRandom rng) {
        return new CreateShipmentMessage(
                "Recipient-" + rng.nextInt(1_000),
                randomAddress(rng),
                randomAddress(rng),
                randomParcel(rng)
        );
    }

    private CreateShipmentMessage.Address randomAddress(ThreadLocalRandom rng) {
        return new CreateShipmentMessage.Address(
                "Street-" + rng.nextInt(100),
                String.valueOf(rng.nextInt(1, 200)),
                rng.nextBoolean() ? String.valueOf(rng.nextInt(1, 50)) : null,
                CITIES[rng.nextInt(CITIES.length)],
                String.format("%05d", rng.nextInt(100_000)),
                COUNTRIES[rng.nextInt(COUNTRIES.length)]
        );
    }

    private CreateShipmentMessage.Parcel randomParcel(ThreadLocalRandom rng) {
        return new CreateShipmentMessage.Parcel(
                rng.nextDouble(1, 200),
                rng.nextDouble(1, 200),
                rng.nextDouble(1, 200),
                rng.nextDouble(0.1, 50),
                rng.nextBoolean(),
                PRIORITIES[rng.nextInt(PRIORITIES.length)]
        );
    }
}
