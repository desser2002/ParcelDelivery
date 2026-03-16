package org.dzianisbova.parceldelivery.vehicle.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

interface VehicleJpaRepository extends JpaRepository<VehicleEntity, UUID> {
    List<VehicleEntity> findAllByStatus(String status);

    @Modifying
    @Query("UPDATE VehicleEntity v SET v.status = :status where v.id= :id")
    void updateStatus(@Param("id") UUID id, @Param("status") String status);

    @Modifying
    @Query("UPDATE VehicleEntity v SET v.packedVolume = v.packedVolume + :addedVolume WHERE v.id = :id")
    void addPackedVolume(@Param("id") UUID id, @Param("addedVolume") double addedVolume);
}
