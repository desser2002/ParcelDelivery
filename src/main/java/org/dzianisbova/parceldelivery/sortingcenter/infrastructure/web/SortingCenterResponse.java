package org.dzianisbova.parceldelivery.sortingcenter.infrastructure.web;

import org.dzianisbova.parceldelivery.sortingcenter.domain.model.SortingCenter;

record SortingCenterResponse(String id, String name) {
    public static SortingCenterResponse from(SortingCenter sortingCenter) {
        return new SortingCenterResponse(sortingCenter.id(), sortingCenter.name());
    }
}
