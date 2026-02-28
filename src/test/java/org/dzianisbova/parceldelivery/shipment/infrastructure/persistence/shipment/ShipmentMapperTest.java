package org.dzianisbova.parceldelivery.shipment.infrastructure.persistence.shipment;

import org.dzianisbova.parceldelivery.domain.model.Dimensions;
import org.dzianisbova.parceldelivery.domain.model.Parcel;
import org.dzianisbova.parceldelivery.domain.model.Priority;
import org.dzianisbova.parceldelivery.shipment.domain.model.Address;
import org.dzianisbova.parceldelivery.shipment.domain.model.Shipment;
import org.dzianisbova.parceldelivery.shipment.domain.model.ShipmentStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ShipmentMapperTest {
    private final ShipmentMapper mapper = new ShipmentMapper();
    private static final UUID ID = UUID.randomUUID();
    private static final UUID SENDER_ID = UUID.randomUUID();
    private static final UUID PARCEL_ID = UUID.randomUUID();
    private static final LocalDateTime CREATED_AT = LocalDateTime.of(2024, 1, 15, 10, 0);

    @Test
    void toDomain_mapsAllFields() {
        ShipmentEntity entity = new ShipmentEntity(
                ID, "TRACK123", SENDER_ID,
                "Pickup St", "1P", "10P", "Warsaw", "00-001", "PL",
                "Alice",
                "Delivery Ave", "99D", "5D", "Krakow", "30-001", "DE",
                PARCEL_ID, 10.0, 20.0, 30.0, 5.5, true, "EXPRESS", "IN_TRANSIT",
                CREATED_AT
        );

        Shipment result = mapper.toDomain(entity);

        assertAll(
                () -> assertEquals(ID, result.getId()),
                () -> assertEquals("TRACK123", result.getTrackingNumber()),
                () -> assertEquals(SENDER_ID, result.getSenderId()),
                () -> assertEquals("Pickup St", result.getPickupAddress().getStreet()),
                () -> assertEquals("1P", result.getPickupAddress().getBuilding()),
                () -> assertEquals("10P", result.getPickupAddress().getApartment()),
                () -> assertEquals("Warsaw", result.getPickupAddress().getCity()),
                () -> assertEquals("00-001", result.getPickupAddress().getPostalCode()),
                () -> assertEquals("PL", result.getPickupAddress().getCountry()),
                () -> assertEquals("Alice", result.getRecipient()),
                () -> assertEquals("Delivery Ave", result.getDeliveryAddress().getStreet()),
                () -> assertEquals("99D", result.getDeliveryAddress().getBuilding()),
                () -> assertEquals("5D", result.getDeliveryAddress().getApartment()),
                () -> assertEquals("Krakow", result.getDeliveryAddress().getCity()),
                () -> assertEquals("30-001", result.getDeliveryAddress().getPostalCode()),
                () -> assertEquals("DE", result.getDeliveryAddress().getCountry()),
                () -> assertEquals(PARCEL_ID.toString(), result.getParcel().getId()),
                () -> assertEquals(10.0, result.getParcel().getDimensions().length()),
                () -> assertEquals(20.0, result.getParcel().getDimensions().width()),
                () -> assertEquals(30.0, result.getParcel().getDimensions().height()),
                () -> assertEquals(5.5, result.getParcel().getWeight()),
                () -> assertTrue(result.getParcel().isFragile()),
                () -> assertEquals(Priority.EXPRESS, result.getParcel().getPriority()),
                () -> assertEquals(ShipmentStatus.IN_TRANSIT, result.getStatus()),
                () -> assertEquals(CREATED_AT, result.getCreatedAt())
        );
    }

    @Test
    void toEntity_mapsAllFields() {
        Shipment shipment = new Shipment(
                ID, "TRACK123", SENDER_ID,
                new Address("Pickup St", "1P", "10P", "Warsaw", "00-001", "PL"),
                "Alice",
                new Address("Delivery Ave", "99D", "5D", "Krakow", "30-001", "DE"),
                new Parcel(PARCEL_ID.toString(), new Dimensions(10.0, 20.0, 30.0), 5.5, true, Priority.EXPRESS),
                CREATED_AT
        );

        ShipmentEntity result = mapper.toEntity(shipment);

        assertAll(
                () -> assertEquals(ID, result.getId()),
                () -> assertEquals("TRACK123", result.getTrackingNumber()),
                () -> assertEquals(SENDER_ID, result.getSenderId()),
                () -> assertEquals("Pickup St", result.getPickupStreet()),
                () -> assertEquals("1P", result.getPickupBuilding()),
                () -> assertEquals("10P", result.getPickupApartment()),
                () -> assertEquals("Warsaw", result.getPickupCity()),
                () -> assertEquals("00-001", result.getPickupPostalCode()),
                () -> assertEquals("PL", result.getPickupCountry()),
                () -> assertEquals("Alice", result.getRecipient()),
                () -> assertEquals("Delivery Ave", result.getDeliveryStreet()),
                () -> assertEquals("99D", result.getDeliveryBuilding()),
                () -> assertEquals("5D", result.getDeliveryApartment()),
                () -> assertEquals("Krakow", result.getDeliveryCity()),
                () -> assertEquals("30-001", result.getDeliveryPostalCode()),
                () -> assertEquals("DE", result.getDeliveryCountry()),
                () -> assertEquals(PARCEL_ID, result.getParcelId()),
                () -> assertEquals(10.0, result.getLength()),
                () -> assertEquals(20.0, result.getWidth()),
                () -> assertEquals(30.0, result.getHeight()),
                () -> assertEquals(5.5, result.getWeight()),
                () -> assertTrue(result.isFragile()),
                () -> assertEquals("EXPRESS", result.getPriority()),
                () -> assertEquals("PENDING", result.getStatus()),
                () -> assertEquals(CREATED_AT, result.getCreatedAt())
        );
    }
}
