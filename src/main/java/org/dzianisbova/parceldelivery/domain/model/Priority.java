package org.dzianisbova.parceldelivery.domain.model;

import lombok.Getter;

@Getter
public enum Priority {
    STANDARD(1),
    HIGH(2);

    private final int value;

    Priority(int value) {
        this.value = value;
    }
}
