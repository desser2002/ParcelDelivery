package org.dzianisbova.parceldelivery.shipment.application;

import lombok.RequiredArgsConstructor;
import org.dzianisbova.parceldelivery.shipment.domain.port.ShipmentRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShipmentService {
    private final ShipmentRepository shipmentRepository;
}
