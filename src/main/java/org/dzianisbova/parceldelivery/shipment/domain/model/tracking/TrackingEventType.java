package org.dzianisbova.parceldelivery.shipment.domain.model.tracking;

public enum TrackingEventType {
    CREATED,                   // null → PENDING
    CONFIRMED,                 // PENDING → CONFIRMED
    ARRIVED_AT_SORTING_CENTER, // CONFIRMED → ARRIVED_AT_SORTING_CENTER
    ASSIGNED_FOR_DELIVERY,     // ARRIVED_AT_SORTING_CENTER → ASSIGNED_FOR_DELIVERY
    DELIVERED,                 // ASSIGNED_FOR_DELIVERY → DELIVERED
    CANCELLED                  // * → CANCELLED
}
