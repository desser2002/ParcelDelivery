package org.dzianisbova.parceldelivery.dispatch.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

interface PackingPlacementJpaRepository extends JpaRepository<PackingPlacementEntity, UUID> {
    @Query(value = """
            SELECT p.pos_x     AS posX,
                   p.pos_y     AS posY,
                   p.pos_z     AS posZ,
                   s.parcel_id AS parcelId,
                   s.length,
                   s.width,
                   s.height,
                   s.weight,
                   s.fragile,
                   s.priority
            FROM vehicle_packing_placements p
            JOIN shipments s ON s.id = p.shipment_id
            WHERE p.vehicle_id = :vehicleId
            """, nativeQuery = true)
    List<PackingPlacementView> findPlacementsByVehicleId(@Param("vehicleId") UUID vehicleId);

    @Modifying
    @Query("DELETE FROM PackingPlacementEntity p WHERE p.vehicleId = :vehicleId")
    void deleteAllByVehicleId(@Param("vehicleId") UUID vehicleId);
}
