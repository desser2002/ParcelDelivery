package org.dzianisbova.parceldelivery.shipment.infrastructure.web;

import jakarta.validation.constraints.Positive;
import org.dzianisbova.parceldelivery.domain.model.Dimensions;
import org.dzianisbova.parceldelivery.domain.model.Parcel;
import org.dzianisbova.parceldelivery.domain.model.Priority;

import java.util.UUID;

public class ParcelDto {
    @Positive
    private double length;

    @Positive
    private double width;

    @Positive
    private double height;

    @Positive
    private double weight;

    private boolean fragile;

    private String priority;

    Parcel toDomain() {
        Priority parcelPriority = priority != null
                ? Priority.valueOf(priority)
                : Priority.STANDARD;

        return new Parcel(
                UUID.randomUUID().toString(),
                new Dimensions(length, width, height),
                weight,
                fragile,
                parcelPriority
        );
    }
}
