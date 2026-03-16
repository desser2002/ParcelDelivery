package org.dzianisbova.parceldelivery.vehicle.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "vehicles")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class VehicleEntity {
    @Id
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(name = "plate_number", nullable = false, unique = true, length = 20)
    private String plateNumber;

    @Column(nullable = false)
    private double length;

    @Column(nullable = false)
    private double width;

    @Column(nullable = false)
    private double height;

    @Column(name = "max_weight", nullable = false)
    private double maxWeight;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "packed_volume", nullable = false)
    private double packedVolume;
}
