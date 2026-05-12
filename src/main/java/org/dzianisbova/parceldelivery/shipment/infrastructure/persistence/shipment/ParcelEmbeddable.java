package org.dzianisbova.parceldelivery.shipment.infrastructure.persistence.shipment;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.dzianisbova.parceldelivery.domain.model.Dimensions;
import org.dzianisbova.parceldelivery.domain.model.Parcel;
import org.dzianisbova.parceldelivery.domain.model.Priority;

import java.util.UUID;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
class ParcelEmbeddable {
    @Column(name = "parcel_id", nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false)
    private double length;

    @Column(nullable = false)
    private double width;

    @Column(nullable = false)
    private double height;

    @Column(nullable = false)
    private double weight;

    @Column(nullable = false)
    private boolean fragile;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Priority priority;

    static ParcelEmbeddable from(Parcel p) {
        Dimensions d = p.getDimensions();
        return new ParcelEmbeddable(
            p.getId(), d.length(), d.width(), d.height(),
            p.getWeight(), p.isFragile(), p.getPriority()
        );
    }

    Parcel toDomain() {
        return new Parcel(
            id,
            new Dimensions(length, width, height),
            weight, fragile, priority
        );
    }
}
