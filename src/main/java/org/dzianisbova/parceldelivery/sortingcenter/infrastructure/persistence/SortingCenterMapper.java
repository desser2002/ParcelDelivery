package org.dzianisbova.parceldelivery.sortingcenter.infrastructure.persistence;

import org.dzianisbova.parceldelivery.sortingcenter.domain.model.SortingCenter;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SortingCenterMapper {
    SortingCenterEntity toEntity(SortingCenter sortingCenter);

    SortingCenter toDomain(SortingCenterEntity sortingCenterEntity);
}
