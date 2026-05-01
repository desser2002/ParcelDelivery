package org.dzianisbova.parceldelivery.shipment.infrastructure.web;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dzianisbova.parceldelivery.shipment.domain.model.Shipment;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
class ShipmentResponse {
    private UUID id;
    private String trackingNumber;
    private String status;
    private String recipient;
    private AddressDto pickupAddress;
    private AddressDto deliveryAddress;
    private LocalDateTime createdAt;

    static ShipmentResponse from(Shipment shipment) {
        return new ShipmentResponse(shipment.getId(),
                shipment.getTrackingNumber(),
                shipment.getStatus().name(),
                shipment.getRecipient(),
                AddressDto.from(shipment.getPickupAddress()),
                AddressDto.from(shipment.getDeliveryAddress()),
                shipment.getCreatedAt());
    }
}
