package org.dzianisbova.parceldelivery.vehicle.infrastructure.web;

import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.dzianisbova.parceldelivery.domain.model.Dimensions;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
class DimensionsDto {
    @Positive
    private double length;
    @Positive
    private double width;
    @Positive
    private double height;

    static Dimensions toDomain(DimensionsDto dto) {
        return new Dimensions(dto.length, dto.width, dto.height);
    }

    static DimensionsDto from(Dimensions d) {
        return new DimensionsDto(d.length(), d.width(), d.height());
    }
}
