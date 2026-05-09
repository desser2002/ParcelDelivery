package org.dzianisbova.parceldelivery.vehicle.infrastructure.web;

import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dzianisbova.parceldelivery.domain.model.Dimensions;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
class DimensionsDto {
    @Positive
    private double length;
    @Positive
    private double width;
    @Positive
    private double height;

    Dimensions toDomain() {
        return new Dimensions(length, width, height);
    }
}
