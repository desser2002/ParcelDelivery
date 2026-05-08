package org.dzianisbova.parceldelivery.integration.tackandtrace;

import org.dzianisbova.parceldelivery.domain.model.Dimensions;
import org.dzianisbova.parceldelivery.domain.model.Parcel;
import org.dzianisbova.parceldelivery.integration.base.BasePostgresIntegrationTest;
import org.dzianisbova.parceldelivery.shipment.application.ShipmentService;
import org.dzianisbova.parceldelivery.shipment.domain.model.Address;
import org.dzianisbova.parceldelivery.shipment.domain.model.Shipment;
import org.dzianisbova.parceldelivery.shipment.domain.model.tracking.TrackingEventType;
import org.dzianisbova.parceldelivery.shipment.domain.port.TrackingEventRepository;
import org.dzianisbova.parceldelivery.shipment.infrastructure.persistence.shipment.ShipmentJpaRepository;
import org.dzianisbova.parceldelivery.shipment.infrastructure.persistence.tracking.TrackingEventJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TrackAndTraceTransactionalIntegrationTest extends BasePostgresIntegrationTest {
    @Autowired
    private ShipmentService shipmentService;

    @Autowired
    private ShipmentJpaRepository shipmentRepository;

    @Autowired
    private TrackingEventJpaRepository trackingEventJpaRepository;

    @Nested
    class WhenNoError {
        @Test
        void shipmentConfirm_shouldSaveBoth_WhenNoError() {
            //given
            Shipment shipment = createShipment();

            //when
            shipmentService.confirmShipment(shipment.getId().toString());

            //then
            assertThat(shipmentRepository.count()).isEqualTo(1);
            assertThat(trackingEventJpaRepository.findAll())
                    .filteredOn(e -> e.getType() == TrackingEventType.CONFIRMED).hasSize(1);
        }

        @Test
        void shipmentCreate_shouldSaveBoth_WhenNoError() {
            //given//when
            createShipment();

            //then
            assertThat(shipmentRepository.count()).isEqualTo(1);
            assertThat(trackingEventJpaRepository.findAll())
                    .filteredOn(e -> e.getType() == TrackingEventType.CREATED).hasSize(1);
        }

        @Test
        void shipmentCancel_shouldSaveBoth_WhenNoError() {
            //given
            Shipment shipment = createShipment();

            //when
            shipmentService.cancelShipment(shipment.getId().toString());
            //then
            assertThat(shipmentRepository.count()).isEqualTo(1);
            assertThat(trackingEventJpaRepository.findAll())
                    .filteredOn(e -> e.getType() == TrackingEventType.CANCELLED).hasSize(1);
        }
    }

    @Nested
    class WhenProjectorError {
        @MockitoBean
        private TrackingEventRepository trackingEventRepository;

        @Test
        void shipmentConfirm_shouldRollBackJustTrackAndTrace_WhenErrorAtEventProjector() {
            //given
            Shipment shipment = createShipment();
            when(trackingEventRepository.save(any())).thenThrow(new RuntimeException("Projector RuntimeException"));

            //when
            shipmentService.confirmShipment(shipment.getId().toString());

            //then
            assertThat(shipmentRepository.count()).isEqualTo(1);
            assertThat(trackingEventJpaRepository.findAll())
                    .filteredOn(e -> e.getType() == TrackingEventType.CONFIRMED).isEmpty();
        }

        @Test
        void shipmentCreate_shouldRollBackJustTrackAndTrace_WhenErrorAtEventProjector() {
            //given//when
            createShipment();
            when(trackingEventRepository.save(any())).thenThrow(new RuntimeException("Projector RuntimeException"));

            //then
            assertThat(shipmentRepository.count()).isEqualTo(1);
            assertThat(trackingEventJpaRepository.findAll())
                    .filteredOn(e -> e.getType() == TrackingEventType.CREATED).isEmpty();
        }

        @Test
        void shipmentCanceled_shouldRollBackJustTrackAndTrace_WhenErrorAtEventProjector() {
            //given
            Shipment shipment = createShipment();
            when(trackingEventRepository.save(any())).thenThrow(new RuntimeException("Projector RuntimeException"));

            //when
            shipment.cancel();
            //then
            assertThat(shipmentRepository.count()).isEqualTo(1);
            assertThat(trackingEventJpaRepository.findAll())
                    .filteredOn(e -> e.getType() == TrackingEventType.CANCELLED).isEmpty();
        }
    }

    private Shipment createShipment() {
        Address pickupAddress = new Address("Main St", "10", "1", "Warsaw", "31-751", "PL");
        Address deliveryAddress = new Address("Second St", "5", "2", "Berlin", "21-111", "DE");

        String parcelId = UUID.randomUUID().toString();
        Dimensions parcelDimensions = new Dimensions(40, 20, 100);
        Parcel parcel = new Parcel(parcelId, parcelDimensions, 2.5);
        return shipmentService.createShipment(pickupAddress, "John Doe", deliveryAddress, parcel);
    }

    //TODO определить какое должно быть поведение при ошибке в shipment
    @AfterEach
    void cleanup() {
        trackingEventJpaRepository.deleteAll();
        shipmentRepository.deleteAll();
    }
}
