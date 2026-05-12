package org.dzianisbova.parceldelivery.sortingcenter.domain.port;

import org.dzianisbova.parceldelivery.sortingcenter.domain.model.SortingCenter;
import org.springframework.stereotype.Repository;

@Repository
public interface SortingCenterRepository {
    SortingCenter save(SortingCenter sortingCenter);

    void deleteAll();
}
