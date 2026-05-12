package org.dzianisbova.parceldelivery.shipment.domain.model;

import org.dzianisbova.parceldelivery.domain.model.Dimensions;
import org.dzianisbova.parceldelivery.domain.model.Parcel;
import org.dzianisbova.parceldelivery.domain.model.Priority;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ShipmentTest {
    private static final String TRACKING = "ABC12345";
    private static final LocalDateTime CREATED_AT = LocalDateTime.now();

    private static Address anyAddress() {
        return new Address("Main St", "1", null, "Warsaw", "00-001", "Poland");
    }

    private static Parcel anyParcel() {
        return new Parcel(new Dimensions(10, 10, 10), 2.0, false, Priority.STANDARD);
    }

    @Test
    void create_buildsPendingShipmentWithAllFields() {
        Address pickup = anyAddress();
        Address delivery = anyAddress();
        Parcel parcel = anyParcel();

        Shipment shipment = Shipment.create(TRACKING, null, pickup, "Alice", delivery, parcel, CREATED_AT);

        assertAll(
            () -> assertNotNull(shipment.getId()),
            () -> assertEquals(TRACKING, shipment.getTrackingNumber()),
            () -> assertEquals(ShipmentStatus.PENDING, shipment.getStatus()),
            () -> assertEquals("Alice", shipment.getRecipient()),
            () -> assertSame(pickup, shipment.getPickupAddress()),
            () -> assertSame(delivery, shipment.getDeliveryAddress()),
            () -> assertEquals(parcel, shipment.getParcel()),
            () -> assertEquals(CREATED_AT, shipment.getCreatedAt()),
            () -> assertNull(shipment.getVehicleId()),
            () -> assertNull(shipment.getSortingCenterId())
        );
    }

    @Nested
    class Validation {
        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   "})
        void rejectsBlankTrackingNumber(String trackingNumber) {
            assertThrows(IllegalArgumentException.class, () ->
                Shipment.create(trackingNumber, null, anyAddress(), "Alice", anyAddress(), anyParcel(), CREATED_AT)
            );
        }

        @Test
        void rejectsNullPickupAddress() {
            assertThrows(IllegalArgumentException.class, () ->
                Shipment.create(TRACKING, null, null, "Alice", anyAddress(), anyParcel(), CREATED_AT)
            );
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   "})
        void rejectsBlankRecipient(String recipient) {
            assertThrows(IllegalArgumentException.class, () ->
                Shipment.create(TRACKING, null, anyAddress(), recipient, anyAddress(), anyParcel(), CREATED_AT)
            );
        }

        @Test
        void rejectsNullDeliveryAddress() {
            assertThrows(IllegalArgumentException.class, () ->
                Shipment.create(TRACKING, null, anyAddress(), "Alice", null, anyParcel(), CREATED_AT)
            );
        }

        @Test
        void rejectsNullParcel() {
            assertThrows(IllegalArgumentException.class, () ->
                Shipment.create(TRACKING, null, anyAddress(), "Alice", anyAddress(), null, CREATED_AT)
            );
        }
    }
}
