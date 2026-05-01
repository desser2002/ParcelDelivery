package org.dzianisbova.parceldelivery.loadgenerator.infrastructure.kafka;

public record CreateShipmentMessage(
        String recipient,
        Address pickupAddress,
        Address deliveryAddress,
        Parcel parcel
) {
    public record Address(
            String street,
            String building,
            String apartment,
            String city,
            String postalCode,
            String country
    ) {}

    public record Parcel(
            double length,
            double width,
            double height,
            double weight,
            boolean fragile,
            String priority
    ) {}
}
