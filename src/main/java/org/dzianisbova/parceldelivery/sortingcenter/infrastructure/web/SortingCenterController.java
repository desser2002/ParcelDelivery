package org.dzianisbova.parceldelivery.sortingcenter.infrastructure.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dzianisbova.parceldelivery.sortingcenter.application.SortingCenterService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sorting-center")
@RequiredArgsConstructor
public class SortingCenterController {
    private final SortingCenterService sortingCenterService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SortingCenterResponse create(@Valid @RequestBody CreateSortingCenterRequest request)
    {
        return (SortingCenterResponse.from(sortingCenterService.createSortingCenter(request.getName())));
    }
}
