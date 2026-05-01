package org.dzianisbova.parceldelivery.dispatch.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "vehicle_packing_placements")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class PackingPlacementEntity {
    @Id
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(name = "vehicle_id", nullable = false)
    private UUID vehicleId;

    @Column(name = "shipment_id", nullable = false)
    private UUID shipmentId;

    @Column(name = "pos_x", nullable = false)
    private double posX;

    @Column(name = "pos_y", nullable = false)
    private double posY;

    @Column(name = "pos_z", nullable = false)
    private double posZ;
}
