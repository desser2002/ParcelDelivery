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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ShipmentTest {

    private static final UUID ID = UUID.randomUUID();
    private static final String TRACKING = "ABC12345";
    private static final LocalDateTime CREATED_AT = LocalDateTime.now();

    private static Address anyAddress() {
        return new Address("Main St", "1", null, "Warsaw", "00-001", "Poland");
    }

    private static Parcel anyParcel() {
        return new Parcel("parcel-1", new Dimensions(10, 10, 10), 2.0, false, Priority.STANDARD);
    }

    @Test
    void constructor_createsPendingShipmentWithAllFields() {
        Address pickup = anyAddress();
        Address delivery = anyAddress();
        Parcel parcel = anyParcel();

        Shipment shipment = new Shipment(ID, TRACKING, null, pickup, "Alice", delivery, parcel, CREATED_AT);

        assertAll(
                () -> assertEquals(ID, shipment.getId()),
                () -> assertEquals(TRACKING, shipment.getTrackingNumber()),
                () -> assertEquals(ShipmentStatus.PENDING, shipment.getStatus()),
                () -> assertEquals("Alice", shipment.getRecipient()),
                () -> assertEquals(pickup, shipment.getPickupAddress()),
                () -> assertEquals(delivery, shipment.getDeliveryAddress()),
                () -> assertEquals(parcel, shipment.getParcel()),
                () -> assertEquals(CREATED_AT, shipment.getCreatedAt())
        );
    }

    @Nested
    class Validation {
        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   "})
        void rejectsBlankTrackingNumber(String trackingNumber) {
            assertThrows(IllegalArgumentException.class, () ->
                    new Shipment(ID, trackingNumber, null, anyAddress(), "Alice", anyAddress(), anyParcel(), CREATED_AT)
            );
        }

        @Test
        void rejectsNullPickupAddress() {
            assertThrows(IllegalArgumentException.class, () ->
                    new Shipment(ID, TRACKING, null, null, "Alice", anyAddress(), anyParcel(), CREATED_AT)
            );
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   "})
        void rejectsBlankRecipient(String recipient) {
            assertThrows(IllegalArgumentException.class, () ->
                    new Shipment(ID, TRACKING, null, anyAddress(), recipient, anyAddress(), anyParcel(), CREATED_AT)
            );
        }

        @Test
        void rejectsNullDeliveryAddress() {
            assertThrows(IllegalArgumentException.class, () ->
                    new Shipment(ID, TRACKING, null, anyAddress(), "Alice", null, anyParcel(), CREATED_AT)
            );
        }

        @Test
        void rejectsNullParcel() {
            assertThrows(IllegalArgumentException.class, () ->
                    new Shipment(ID, TRACKING, null, anyAddress(), "Alice", anyAddress(), null, CREATED_AT)
            );
        }
    }
}
