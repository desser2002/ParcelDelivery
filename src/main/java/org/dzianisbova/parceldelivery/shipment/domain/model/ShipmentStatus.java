package org.dzianisbova.parceldelivery.shipment.domain.model;

public enum ShipmentStatus {
    PENDING,
    CONFIRMED,
    ARRIVED_AT_SORTING_CENTER,
    ASSIGNED_FOR_DELIVERY,
    DELIVERED,
    CANCELLED
}