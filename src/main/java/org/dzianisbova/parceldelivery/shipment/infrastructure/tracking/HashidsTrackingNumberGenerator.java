package org.dzianisbova.parceldelivery.shipment.infrastructure.tracking;

import org.dzianisbova.parceldelivery.shipment.domain.port.TrackingIdSource;
import org.dzianisbova.parceldelivery.shipment.domain.port.TrackingNumberGenerator;
import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
class HashidsTrackingNumberGenerator implements TrackingNumberGenerator {
    private final TrackingIdSource trackingIdSource;
    private final Hashids hashids;

    HashidsTrackingNumberGenerator(TrackingIdSource trackingIdSource,
                                   @Value("${tracking.hashids.salt}") String salt,
                                   @Value("${tracking.hashids.min-length}") int minLength) {
        this.trackingIdSource = trackingIdSource;
        this.hashids = new Hashids(salt, minLength, "0123456789ABCDEF");
    }

    @Override
    public String generate() {
        return hashids.encode(trackingIdSource.next());
    }
}
