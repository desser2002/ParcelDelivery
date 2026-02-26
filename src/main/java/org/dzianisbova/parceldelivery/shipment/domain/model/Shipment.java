package org.dzianisbova.parceldelivery.shipment.domain.model;

import lombok.Getter;
import org.dzianisbova.parceldelivery.domain.model.Parcel;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Shipment {
    private final UUID id;
    private final UUID senderId;
    private String trackingNumber;


    private final Address pickupAddress;
    private final Address deliveryAddress;
    private final String recipient;

    private final Parcel parcel;

    private ShipmentStatus status;
    private final LocalDateTime createdAt;

    public Shipment(UUID id,
                    String trackingNumber,
                    UUID senderId,
                    Address pickupAddress,
                    String recipient,
                    Address deliveryAddress,
                    Parcel parcel,
                    LocalDateTime createdAt) {
        this(id, trackingNumber, senderId, pickupAddress, recipient, deliveryAddress, parcel, ShipmentStatus.PENDING, createdAt);
    }

    public Shipment(UUID id,
                    String trackingNumber,
                    UUID senderId,
                    Address pickupAddress,
                    String recipient,
                    Address deliveryAddress,
                    Parcel parcel,
                    ShipmentStatus status,
                    LocalDateTime createdAt) {
        validate(trackingNumber, pickupAddress, recipient, deliveryAddress, parcel);
        this.id = id;
        this.trackingNumber = trackingNumber;
        this.senderId = senderId;
        this.pickupAddress = pickupAddress;
        this.recipient = recipient;
        this.deliveryAddress = deliveryAddress;
        this.parcel = parcel;
        this.status = status;
        this.createdAt = createdAt;
    }

    private void validate(String trackingNumber,
                          Address pickupAddress,
                          String recipient,
                          Address deliveryAddress,
                          Parcel parcel) {
        if (trackingNumber == null || trackingNumber.isBlank()) {
            throw new IllegalArgumentException("Tracking number cannot be empty");
        }
        if (pickupAddress == null) {
            throw new IllegalArgumentException("Pickup address cannot be null");
        }
        if (recipient == null || recipient.isBlank()) {
            throw new IllegalArgumentException("Recipient cannot be empty");
        }
        if (deliveryAddress == null) {
            throw new IllegalArgumentException("Delivery address cannot be null");
        }
        if (parcel == null) {
            throw new IllegalArgumentException("Parcel cannot be null");
        }
    }
}
