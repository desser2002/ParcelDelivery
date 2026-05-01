package org.dzianisbova.parceldelivery.loadgenerator.infrastructure;

record CreateShipmentMessage(
        String recipient,
        Address pickupAddress,
        Address deliveryAddress,
        Parcel parcel
) {
}