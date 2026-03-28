package org.dzianisbova.parceldelivery.shipment.application;

public class ShipmentNotFoundException extends RuntimeException {
    public ShipmentNotFoundException(String trackingNumber) {
        super("Shipment not found: " + trackingNumber);
    }
}
