package org.dzianisbova.parceldelivery.shipment.application;

import org.dzianisbova.parceldelivery.domain.model.Dimensions;
import org.dzianisbova.parceldelivery.domain.model.Parcel;
import org.dzianisbova.parceldelivery.domain.model.Priority;
import org.dzianisbova.parceldelivery.shipment.domain.model.Address;
import org.dzianisbova.parceldelivery.shipment.domain.model.Shipment;
import org.dzianisbova.parceldelivery.shipment.domain.port.ShipmentRepository;
import org.dzianisbova.parceldelivery.shipment.domain.port.TrackingNumberGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShipmentServiceTest {
    @Mock
    private ShipmentRepository shipmentRepository;
    @Mock
    private TrackingNumberGenerator trackingNumberGenerator;
    @InjectMocks
    private ShipmentService shipmentService;

    private static Address pickupAddress() {
        return new Address("Pickup St", "1", null, "Warsaw", "00-001", "Poland");
    }

    private static Address deliveryAddress() {
        return new Address("Delivery Ave", "99", null, "Krakow", "30-001", "Poland");
    }

    private static Parcel anyParcel() {
        return new Parcel("parcel-1", new Dimensions(10, 10, 10), 2.0, false, Priority.STANDARD);
    }

    @Nested
    class CreateShipment {
        @Test
        void savesShipmentWithGeneratedTrackingNumberAndCorrectFields() {
            Address pickup = pickupAddress();
            Address delivery = deliveryAddress();
            Parcel parcel = anyParcel();
            when(trackingNumberGenerator.generate()).thenReturn("TRACK123");
            when(shipmentRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
            ArgumentCaptor<Shipment> captor = ArgumentCaptor.forClass(Shipment.class);

            shipmentService.createShipment(pickup, "Alice", delivery, parcel);

            verify(shipmentRepository).save(captor.capture());
            Shipment saved = captor.getValue();
            assertAll(
                    () -> assertEquals("TRACK123", saved.getTrackingNumber()),
                    () -> assertEquals("Alice", saved.getRecipient()),
                    () -> assertEquals(pickup, saved.getPickupAddress()),
                    () -> assertEquals(delivery, saved.getDeliveryAddress()),
                    () -> assertEquals(parcel, saved.getParcel())
            );
        }
    }

    @Nested
    class FindByTrackingNumber {
        @Test
        void throwsShipmentNotFoundExceptionWhenMissing() {
            when(shipmentRepository.findByTrackingNumber("UNKNOWN")).thenReturn(Optional.empty());

            ShipmentNotFoundException ex = assertThrows(ShipmentNotFoundException.class,
                    () -> shipmentService.findByTrackingNumber("UNKNOWN"));

            assertTrue(ex.getMessage().contains("UNKNOWN"));
        }
    }
}
