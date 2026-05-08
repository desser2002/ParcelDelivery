package org.dzianisbova.parceldelivery.shipment.domain.model;

import lombok.AccessLevel;
import lombok.Getter;
import org.dzianisbova.parceldelivery.domain.model.Parcel;
import org.dzianisbova.parceldelivery.shipment.domain.event.ShipmentAssignedForDeliveryEvent;
import org.dzianisbova.parceldelivery.shipment.domain.event.ShipmentConfirmedEvent;
import org.dzianisbova.parceldelivery.shipment.domain.event.ShipmentCreatedEvent;
import org.dzianisbova.parceldelivery.shipment.domain.event.ShipmentEvent;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Shipment {
    private final UUID id;
    private final UUID senderId;
    private final String trackingNumber;

    private final Address pickupAddress;
    private final Address deliveryAddress;
    private final String recipient;

    private final Parcel parcel;

    private ShipmentStatus status;
    private final LocalDateTime createdAt;
    private UUID vehicleId;

    @Getter(AccessLevel.NONE)
    private final List<ShipmentEvent> events = new ArrayList<>();

    private Shipment(UUID id,
                     String trackingNumber,
                     UUID senderId,
                     Address pickupAddress,
                     String recipient,
                     Address deliveryAddress,
                     Parcel parcel,
                     ShipmentStatus status,
                     LocalDateTime createdAt,
                     UUID vehicleId) {
        validate(trackingNumber, pickupAddress, recipient, deliveryAddress, parcel);
        this.id = id;
        this.trackingNumber = trackingNumber;
        this.senderId = senderId;
        this.pickupAddress = pickupAddress;
        this.deliveryAddress = deliveryAddress;
        this.recipient = recipient;
        this.parcel = parcel;
        this.status = status;
        this.createdAt = createdAt;
        this.vehicleId = vehicleId;
    }

    // TODO скррее всего этот метод будет переделан под получение сущности из базы
    public static Shipment create(UUID id,
                                  String trackingNumber,
                                  UUID senderId,
                                  Address pickupAddress,
                                  String recipient,
                                  Address deliveryAddress,
                                  Parcel parcel,
                                  ShipmentStatus status,
                                  LocalDateTime createdAt,
                                  UUID vehicleId) {
        return new Shipment(id, trackingNumber, senderId, pickupAddress, recipient,
                deliveryAddress, parcel, status, createdAt, vehicleId);
    }

    public static Shipment create(UUID id,
                                  String trackingNumber,
                                  UUID senderId,
                                  Address pickupAddress,
                                  String recipient,
                                  Address deliveryAddress,
                                  Parcel parcel,
                                  LocalDateTime createdAt) {
        Shipment shipment = new Shipment(id, trackingNumber, senderId, pickupAddress, recipient,
                deliveryAddress, parcel, ShipmentStatus.PENDING, createdAt, null);

        shipment.events.add(new ShipmentCreatedEvent(shipment.id, Instant.now()));
        return shipment;
    }

    public void confirm() {
        if (this.status != ShipmentStatus.PENDING) {
            throw new IllegalStateException(
                    "Cannot confirm shipment " + this.id + ": expected status PENDING but was " + this.status
            );
        }
        this.status = ShipmentStatus.CONFIRMED;
        events.add(new ShipmentConfirmedEvent(id, Instant.now()));
    }

    public void assignForDelivery(UUID vehicleId) {
        if (this.status != ShipmentStatus.ARRIVED_AT_SORTING_CENTER) {
            throw new IllegalStateException(
                    "Cannot assign for delivery shipment: " + id
                            + " expected status ARRIVED_AT_SORTING_CENTER but get: " + this.status
            );
        }

        this.status = ShipmentStatus.ASSIGNED_FOR_DELIVERY;
        this.vehicleId = vehicleId;
        events.add(new ShipmentAssignedForDeliveryEvent(id, vehicleId, Instant.now()));
    }

    public List<ShipmentEvent> pullEvents() {
        var copy = List.copyOf(events);
        events.clear();
        return copy;
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
