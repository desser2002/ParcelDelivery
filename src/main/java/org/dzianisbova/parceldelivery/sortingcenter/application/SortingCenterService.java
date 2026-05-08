package org.dzianisbova.parceldelivery.sortingcenter.application;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.dzianisbova.parceldelivery.sortingcenter.domain.model.SortingCenter;
import org.dzianisbova.parceldelivery.sortingcenter.domain.port.SortingCenterRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SortingCenterService {
    private final SortingCenterRepository sortingCenterRepository;

    public SortingCenter createSortingCenter(@NotBlank String name) {
        SortingCenter sortingCenter = new SortingCenter(UUID.randomUUID().toString(), name);
        return sortingCenterRepository.save(sortingCenter);
    }
}
