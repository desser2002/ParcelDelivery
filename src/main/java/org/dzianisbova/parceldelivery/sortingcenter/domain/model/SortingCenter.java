package org.dzianisbova.parceldelivery.sortingcenter.domain.model;

import java.util.UUID;

public record SortingCenter(UUID id, String name) {
    public SortingCenter(String name) {
        this(UUID.randomUUID(), name);
    }
}
