package org.dzianisbova.parceldelivery.shipment.infrastructure.persistence.shipment;

import org.dzianisbova.parceldelivery.shipment.domain.model.Shipment;
import org.dzianisbova.parceldelivery.shipment.domain.model.ShipmentStatus;
import org.springframework.stereotype.Component;

@Component
class ShipmentMapper {
    public Shipment toDomain(ShipmentEntity e) {
        return Shipment.restore(
            e.getId(),
            e.getTrackingNumber(),
            e.getSenderId(),
            e.getPickup().toDomain(),
            e.getRecipient(),
            e.getDelivery().toDomain(),
            e.getParcel().toDomain(),
            ShipmentStatus.valueOf(e.getStatus()),
            e.getCreatedAt(),
            e.getVehicleId(),
            e.getSortingCenterId()
        );
    }

    public ShipmentEntity toEntity(Shipment s) {
        return new ShipmentEntity(
            s.getId(),
            s.getTrackingNumber(),
            s.getSenderId(),
            AddressEmbeddable.from(s.getPickupAddress()),
            s.getRecipient(),
            AddressEmbeddable.from(s.getDeliveryAddress()),
            ParcelEmbeddable.from(s.getParcel()),
            s.getStatus().name(),
            s.getCreatedAt(),
            s.getVehicleId(),
            s.getSortingCenterId()
        );
    }
}
