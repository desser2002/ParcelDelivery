package org.dzianisbova.parceldelivery.vehicle.infrastructure.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dzianisbova.parceldelivery.vehicle.application.VehicleService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vehicles")
@RequiredArgsConstructor
public class VehicleController {
    private final VehicleService vehicleService;

    @PostMapping
    public void create(@Valid @RequestBody CreateVehicleRequest request) {
        vehicleService.create(request.getPlateNumber(), request.getDimensions().toDomain(), request.getMaxWeight());
    }
}
