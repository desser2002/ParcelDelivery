package org.dzianisbova.parceldelivery.loadgenerator.application;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentLinkedQueue;

@Component
@ConditionalOnProperty(name = "loadgenerator.enabled", havingValue = "true")
public class TrackingNumberRegistry {
    private final ConcurrentLinkedQueue<String> trackingNumbers = new ConcurrentLinkedQueue<>();

    public void register(String trackingNumber) {
        trackingNumbers.add(trackingNumber);
    }

    public String poll() {
        return trackingNumbers.poll();
    }

    public boolean isEmpty() {
        return trackingNumbers.isEmpty();
    }
}
