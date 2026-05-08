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

class TrackAndTraceTransactionalIntegrationTest extends BasePostgresIntegrationTest {
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
            String shipmentId = createShipment().getId().toString();

            //when
            shipmentService.confirmShipment(shipmentId);

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
            String shipmentId = createShipment().getId().toString();

            //when
            shipmentService.cancelShipment(shipmentId);
            //then
            assertThat(shipmentRepository.count()).isEqualTo(1);
            assertThat(trackingEventJpaRepository.findAll())
                    .filteredOn(e -> e.getType() == TrackingEventType.CANCELLED).hasSize(1);
        }

        @Test
        void shipmentMarkArrived_shouldSaveBoth_WhenNoError() {
            //given
            String shipmentId = createShipment().getId().toString();
            shipmentService.confirmShipment(shipmentId);
            //when
            shipmentService.markArrived(shipmentId);
            //then
            assertThat(shipmentRepository.count()).isEqualTo(1);
            assertThat(trackingEventJpaRepository.findAll())
                    .filteredOn(e -> e.getType() == TrackingEventType.ARRIVED_AT_SORTING_CENTER).hasSize(1);
        }
    }

    @Nested
    class WhenProjectorError {
        @MockitoBean
        private TrackingEventRepository trackingEventRepository;

        @Test
        void shipmentConfirm_shouldRollBackJustTrackAndTrace_WhenErrorAtEventProjector() {
            //given
            String shipmentId = createShipment().getId().toString();
            when(trackingEventRepository.save(any())).thenThrow(new RuntimeException("Projector RuntimeException"));

            //when
            shipmentService.confirmShipment(shipmentId);

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
            String shipmentId = createShipment().getId().toString();
            when(trackingEventRepository.save(any())).thenThrow(new RuntimeException("Projector RuntimeException"));

            //when
            shipmentService.cancelShipment(shipmentId);
            //then
            assertThat(shipmentRepository.count()).isEqualTo(1);
            assertThat(trackingEventJpaRepository.findAll())
                    .filteredOn(e -> e.getType() == TrackingEventType.CANCELLED).isEmpty();
        }

        @Test
        void shipmentMarkArrived_shouldRollBackJustTrackAndTrace_WhenErrorAtEventProjector() {
            //given
            String shipmentId = createShipment().getId().toString();
            when(trackingEventRepository.save(any())).thenThrow(new RuntimeException("Projector RuntimeException"));
            shipmentService.confirmShipment(shipmentId);
            //when
            shipmentService.markArrived(shipmentId);
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
