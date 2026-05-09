package org.dzianisbova.parceldelivery.validation.platenumber.strategys;

import org.dzianisbova.parceldelivery.validation.platenumber.PlateRegion;

public interface PlateValidationStrategy {
    PlateRegion getRegion();

    boolean isValid(String plateNumber);
}
