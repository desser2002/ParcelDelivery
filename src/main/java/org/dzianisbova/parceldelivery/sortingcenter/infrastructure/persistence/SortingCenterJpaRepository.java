package org.dzianisbova.parceldelivery.sortingcenter.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SortingCenterJpaRepository extends JpaRepository<SortingCenterEntity, UUID> {

}
