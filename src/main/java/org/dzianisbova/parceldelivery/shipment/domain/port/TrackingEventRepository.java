package org.dzianisbova.parceldelivery.shipment.domain.port;

import org.dzianisbova.parceldelivery.shipment.domain.model.tracking.TrackingEvent;

public interface TrackingEventRepository  {
    TrackingEvent save(TrackingEvent trackingEvent);
}
