package org.dzianisbova.parceldelivery.shipment.infrastructure.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dzianisbova.parceldelivery.shipment.application.ShipmentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

    @PostMapping("/{id}/confirm")
    public ShipmentResponse confirm(@PathVariable UUID id) {
        return ShipmentResponse.from(shipmentService.confirmShipment(id));
    }

    @PostMapping("/{id}/mark-arrived")
    public ShipmentResponse markArrivedToSortingCenter(@PathVariable UUID id,
                                                       @RequestBody MarkArrivedRequest request) {
        return ShipmentResponse.from(shipmentService
            .markArrived(id, request.sortingCenterId()));
    }

    @PostMapping("/{id}/pack")
    public ShipmentResponse pack(@PathVariable UUID id, @RequestBody AssignForDeliveryRequest request) {
        return ShipmentResponse.from(shipmentService
            .assignForDelivery(id, request.vehicleId()));
    }

    @PostMapping("/{id}/mark-delivered")
    public ShipmentResponse markDelivered(@PathVariable UUID id) {
        return ShipmentResponse.from(shipmentService.markDelivered(id));
    }

    @PostMapping("/{id}/cancel")
    public ShipmentResponse cancel(@PathVariable UUID id) {
        return ShipmentResponse.from(shipmentService.cancelShipment(id));
    }
}
