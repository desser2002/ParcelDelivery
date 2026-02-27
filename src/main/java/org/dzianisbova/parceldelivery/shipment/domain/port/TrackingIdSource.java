package org.dzianisbova.parceldelivery.shipment.domain.port;

public interface TrackingIdSource {
    long next();
}
