package org.dzianisbova.parceldelivery.validation.platenumber.strategys;

import org.dzianisbova.parceldelivery.validation.platenumber.PlateRegion;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class PolandPlateValidationStrategy implements PlateValidationStrategy {
    private static final Pattern PATTERN = Pattern.compile(
        "^[A-Z]{2,3}[ACEFHJKLMNPRSTUVWXY0-9]{4,5}$"
    );

    @Override
    public PlateRegion getRegion() {
        return PlateRegion.POLAND;
    }

    @Override
    public boolean isValid(String plateNumber) {
        if (plateNumber == null || plateNumber.isBlank()) {
            return false;
        }
        return PATTERN.matcher(plateNumber).matches();
    }
}
