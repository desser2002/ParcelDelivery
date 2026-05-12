package org.dzianisbova.parceldelivery.sortingcenter.infrastructure.persistence;

import lombok.RequiredArgsConstructor;
import org.dzianisbova.parceldelivery.sortingcenter.domain.model.SortingCenter;
import org.dzianisbova.parceldelivery.sortingcenter.domain.port.SortingCenterRepository;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class SortingCenterRepositoryAdapter implements SortingCenterRepository {
    private final SortingCenterJpaRepository sortingCenterJpaRepository;
    private final SortingCenterMapper mapper;

    @Override
    public SortingCenter save(SortingCenter sortingCenter) {
        return mapper.toDomain(sortingCenterJpaRepository.save(mapper.toEntity(sortingCenter)));
    }

    @Override
    public void deleteAll() {
        sortingCenterJpaRepository.deleteAll();
    }
}
