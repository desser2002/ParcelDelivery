package org.dzianisbova.parceldelivery.vehicle.infrastructure.web;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.dzianisbova.parceldelivery.domain.model.Vehicle;
import org.dzianisbova.parceldelivery.validation.platenumber.PlateNumber;
import org.dzianisbova.parceldelivery.validation.platenumber.PlateRegion;

@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class CreateVehicleRequest {
    @NotNull
    @PlateNumber(region = PlateRegion.POLAND)
    private String plateNumber;

    @Valid
    @NotNull
    private DimensionsDto dimensions;

    @Positive
    private double maxWeight;

    static Vehicle toDomain(CreateVehicleRequest request) {
        return new Vehicle(request.plateNumber, DimensionsDto.toDomain(request.dimensions), request.maxWeight);
    }
}
