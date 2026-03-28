package org.dzianisbova.parceldelivery.shipment.application;

import lombok.RequiredArgsConstructor;
import org.dzianisbova.parceldelivery.domain.model.Parcel;
import org.dzianisbova.parceldelivery.shipment.domain.model.Address;
import org.dzianisbova.parceldelivery.shipment.domain.model.Shipment;
import org.dzianisbova.parceldelivery.shipment.domain.port.ShipmentRepository;
import org.dzianisbova.parceldelivery.shipment.domain.port.TrackingNumberGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShipmentService {
    private final ShipmentRepository shipmentRepository;
    private final TrackingNumberGenerator trackingNumberGenerator;

    @Transactional
    public Shipment createShipment(Address pickupAddress,
                                   String recipient,
                                   Address deliveryAddress,
                                   Parcel parcel) {
        Shipment shipment = new Shipment(
                UUID.randomUUID(),
                trackingNumberGenerator.generate(),
                null,
                pickupAddress,
                recipient,
                deliveryAddress,
                parcel,
                LocalDateTime.now()
        );
        return shipmentRepository.save(shipment);
    }

    @Transactional(readOnly = true)
    public Shipment findByTrackingNumber(String trackingNumber) {
        return shipmentRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new ShipmentNotFoundException(trackingNumber));
    }
}
