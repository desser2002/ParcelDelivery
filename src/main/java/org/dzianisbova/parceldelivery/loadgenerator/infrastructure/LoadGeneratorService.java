package org.dzianisbova.parceldelivery.loadgenerator.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
class LoadGeneratorService {
    private static final String TOPIC = "create-shipment-requests";
    private static final String[] CITIES = {"Warsaw", "Berlin", "Paris", "Minsk", "Prague"};
    private static final String[] COUNTRIES = {"Poland", "Germany", "France", "Belarus", "Czech Republic"};
    private static final String[] PRIORITIES = {"STANDARD", "EXPRESS"};

    private final KafkaTemplate<String, CreateShipmentMessage> kafkaTemplate;

    void generate(int count) {
        ThreadLocalRandom rng = ThreadLocalRandom.current();
        for (int i = 0; i < count; i++) {
            kafkaTemplate.send(TOPIC, buildMessage(rng));
        }
    }

    private CreateShipmentMessage buildMessage(ThreadLocalRandom rng) {
        return new CreateShipmentMessage(
                "Recepient-" + rng.nextInt(1_000),
                randomAddress(rng),
                randomAddress(rng),
                randomParcel(rng)
        );
    }

    private Address randomAddress(ThreadLocalRandom rng) {
        return new Address(
                "Street-" + rng.nextInt(100),
                String.valueOf(rng.nextInt(1, 200)),
                rng.nextBoolean() ? String.valueOf(rng.nextInt(1, 50)) : null,
                CITIES[rng.nextInt(CITIES.length)],
                String.format("%05d", rng.nextInt(100_000)),
                COUNTRIES[rng.nextInt(COUNTRIES.length)]
        );
    }

    private Parcel randomParcel(ThreadLocalRandom rng) {
        return new Parcel(rng.nextDouble(1, 200),
                rng.nextDouble(1, 200),
                rng.nextDouble(1, 200),
                rng.nextDouble(0.1, 50),
                rng.nextBoolean(),
                PRIORITIES[rng.nextInt(PRIORITIES.length)]);
    }
}
