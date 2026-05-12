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
    private static final UUID VEHICLE_ID = UUID.randomUUID();
    private static final UUID SORTING_CENTER_ID = UUID.randomUUID();
    private static final LocalDateTime CREATED_AT = LocalDateTime.of(2024, 1, 15, 10, 0);

    @Test
    void toDomain_mapsTopLevelFields() {
        Shipment result = mapper.toDomain(sampleEntity());

        assertAll(
            () -> assertEquals(ID, result.getId()),
            () -> assertEquals("TRACK123", result.getTrackingNumber()),
            () -> assertEquals(SENDER_ID, result.getSenderId()),
            () -> assertEquals("Alice", result.getRecipient()),
            () -> assertEquals(ShipmentStatus.ARRIVED_AT_SORTING_CENTER, result.getStatus()),
            () -> assertEquals(CREATED_AT, result.getCreatedAt()),
            () -> assertEquals(VEHICLE_ID, result.getVehicleId()),
            () -> assertEquals(SORTING_CENTER_ID, result.getSortingCenterId())
        );
    }

    @Test
    void toDomain_mapsPickupAddress() {
        Address pickup = mapper.toDomain(sampleEntity()).getPickupAddress();

        assertAll(
            () -> assertEquals("Pickup St", pickup.getStreet()),
            () -> assertEquals("1P", pickup.getBuilding()),
            () -> assertEquals("10P", pickup.getApartment()),
            () -> assertEquals("Warsaw", pickup.getCity()),
            () -> assertEquals("00-001", pickup.getPostalCode()),
            () -> assertEquals("PL", pickup.getCountry())
        );
    }

    @Test
    void toDomain_mapsDeliveryAddress() {
        Address delivery = mapper.toDomain(sampleEntity()).getDeliveryAddress();

        assertAll(
            () -> assertEquals("Delivery Ave", delivery.getStreet()),
            () -> assertEquals("99D", delivery.getBuilding()),
            () -> assertEquals("5D", delivery.getApartment()),
            () -> assertEquals("Krakow", delivery.getCity()),
            () -> assertEquals("30-001", delivery.getPostalCode()),
            () -> assertEquals("DE", delivery.getCountry())
        );
    }

    @Test
    void toDomain_mapsParcel() {
        Parcel parcel = mapper.toDomain(sampleEntity()).getParcel();

        assertAll(
            () -> assertEquals(PARCEL_ID, parcel.getId()),
            () -> assertEquals(10.0, parcel.getDimensions().length()),
            () -> assertEquals(20.0, parcel.getDimensions().width()),
            () -> assertEquals(30.0, parcel.getDimensions().height()),
            () -> assertEquals(5.5, parcel.getWeight()),
            () -> assertTrue(parcel.isFragile()),
            () -> assertEquals(Priority.EXPRESS, parcel.getPriority())
        );
    }

    @Test
    void toEntity_mapsTopLevelFields() {
        ShipmentEntity result = mapper.toEntity(sampleShipment());

        assertAll(
            () -> assertEquals(ID, result.getId()),
            () -> assertEquals("TRACK123", result.getTrackingNumber()),
            () -> assertEquals(SENDER_ID, result.getSenderId()),
            () -> assertEquals("Alice", result.getRecipient()),
            () -> assertEquals("PENDING", result.getStatus()),
            () -> assertEquals(CREATED_AT, result.getCreatedAt()),
            () -> assertEquals(VEHICLE_ID, result.getVehicleId()),
            () -> assertEquals(SORTING_CENTER_ID, result.getSortingCenterId())
        );
    }

    @Test
    void toEntity_mapsPickupAddress() {
        AddressEmbeddable pickup = mapper.toEntity(sampleShipment()).getPickup();

        assertAll(
            () -> assertEquals("Pickup St", pickup.getStreet()),
            () -> assertEquals("1P", pickup.getBuilding()),
            () -> assertEquals("10P", pickup.getApartment()),
            () -> assertEquals("Warsaw", pickup.getCity()),
            () -> assertEquals("00-001", pickup.getPostalCode()),
            () -> assertEquals("PL", pickup.getCountry())
        );
    }

    @Test
    void toEntity_mapsDeliveryAddress() {
        AddressEmbeddable delivery = mapper.toEntity(sampleShipment()).getDelivery();

        assertAll(
            () -> assertEquals("Delivery Ave", delivery.getStreet()),
            () -> assertEquals("99D", delivery.getBuilding()),
            () -> assertEquals("5D", delivery.getApartment()),
            () -> assertEquals("Krakow", delivery.getCity()),
            () -> assertEquals("30-001", delivery.getPostalCode()),
            () -> assertEquals("DE", delivery.getCountry())
        );
    }

    @Test
    void toEntity_mapsParcel() {
        ParcelEmbeddable parcel = mapper.toEntity(sampleShipment()).getParcel();

        assertAll(
            () -> assertEquals(PARCEL_ID, parcel.getId()),
            () -> assertEquals(10.0, parcel.getLength()),
            () -> assertEquals(20.0, parcel.getWidth()),
            () -> assertEquals(30.0, parcel.getHeight()),
            () -> assertEquals(5.5, parcel.getWeight()),
            () -> assertTrue(parcel.isFragile()),
            () -> assertEquals(Priority.EXPRESS, parcel.getPriority())
        );
    }

    private ShipmentEntity sampleEntity() {
        return new ShipmentEntity(
            ID, "TRACK123", SENDER_ID,
            new AddressEmbeddable("Pickup St", "1P", "10P", "Warsaw", "00-001", "PL"),
            "Alice",
            new AddressEmbeddable("Delivery Ave", "99D", "5D", "Krakow", "30-001", "DE"),
            new ParcelEmbeddable(PARCEL_ID, 10.0, 20.0, 30.0, 5.5, true, Priority.EXPRESS),
            "ARRIVED_AT_SORTING_CENTER", CREATED_AT, VEHICLE_ID, SORTING_CENTER_ID
        );
    }

    private Shipment sampleShipment() {
        return Shipment.restore(
            ID, "TRACK123", SENDER_ID,
            new Address("Pickup St", "1P", "10P", "Warsaw", "00-001", "PL"),
            "Alice",
            new Address("Delivery Ave", "99D", "5D", "Krakow", "30-001", "DE"),
            new Parcel(PARCEL_ID, new Dimensions(10.0, 20.0, 30.0), 5.5, true, Priority.EXPRESS),
            ShipmentStatus.PENDING, CREATED_AT, VEHICLE_ID, SORTING_CENTER_ID
        );
    }
}
