package org.dzianisbova.parceldelivery.shipment.infrastructure.persistence.shipment;

import org.dzianisbova.parceldelivery.domain.model.Dimensions;
import org.dzianisbova.parceldelivery.domain.model.Parcel;
import org.dzianisbova.parceldelivery.domain.model.Priority;
import org.dzianisbova.parceldelivery.shipment.domain.model.Address;
import org.dzianisbova.parceldelivery.shipment.domain.model.Shipment;
import org.springframework.stereotype.Component;

@Component
public class ShipmentMapper {
    public Shipment toDomain(ShipmentEntity entity) {
        Address pickupAddress = new Address(
                entity.getPickupStreet(),
                entity.getPickupBuilding(),
                entity.getPickupApartment(),
                entity.getPickupCity(),
                entity.getPickupPostalCode(),
                entity.getPickupCountry()
        );

        Address deliveryAddress = new Address(
                entity.getDeliveryStreet(),
                entity.getDeliveryBuilding(),
                entity.getDeliveryApartment(),
                entity.getDeliveryCity(),
                entity.getDeliveryPostalCode(),
                entity.getDeliveryCountry()
        );

        Parcel parcel = new Parcel(
                entity.getId().toString(),
                new Dimensions(entity.getLength(), entity.getWidth(), entity.getHeight()),
                entity.getWeight(),
                entity.isFragile(),
                Priority.valueOf(entity.getPriority())
        );

        return new Shipment(
                entity.getId(),
                entity.getTrackingNumber(),
                entity.getSenderId(),
                pickupAddress,
                entity.getRecipient(),
                deliveryAddress,
                parcel
        );
    }

    public ShipmentEntity toEntity(Shipment shipment) {
        return new ShipmentEntity(
                shipment.getId(),
                shipment.getTrackingNumber(),
                shipment.getSenderId(),
                shipment.getPickupAddress().getStreet(),
                shipment.getPickupAddress().getBuilding(),
                shipment.getPickupAddress().getApartment(),
                shipment.getPickupAddress().getCity(),
                shipment.getPickupAddress().getPostalCode(),
                shipment.getPickupAddress().getCountry(),
                shipment.getRecipient(),
                shipment.getDeliveryAddress().getStreet(),
                shipment.getDeliveryAddress().getBuilding(),
                shipment.getDeliveryAddress().getApartment(),
                shipment.getDeliveryAddress().getCity(),
                shipment.getDeliveryAddress().getPostalCode(),
                shipment.getDeliveryAddress().getCountry(),
                shipment.getParcel().getDimensions().length(),
                shipment.getParcel().getDimensions().width(),
                shipment.getParcel().getDimensions().height(),
                shipment.getParcel().getWeight(),
                shipment.getParcel().isFragile(),
                shipment.getParcel().getPriority().name(),
                shipment.getStatus().name(),
                shipment.getCreatedAt()
        );
    }
}
