package org.dzianisbova.parceldelivery.shipment.infrastructure.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dzianisbova.parceldelivery.shipment.application.ShipmentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shipments")
@RequiredArgsConstructor
public class ShipmentController {
    private final ShipmentService shipmentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ShipmentResponse create(@Valid @RequestBody CreateShipmentRequest request) {
        return ShipmentResponse.from(
                shipmentService.createShipment(
                        request.getPickupAddress().toDomain(),
                        request.getRecipient(),
                        request.getDeliveryAddress().toDomain(),
                        request.getParcel().toDomain()
                )
        );
    }

    @GetMapping("/{trackingNumber}")
    public ShipmentResponse trackShipment(@PathVariable String trackingNumber) {
        return ShipmentResponse.from(
                shipmentService.findByTrackingNumber(trackingNumber)
        );
    }
}
