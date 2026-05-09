package org.dzianisbova.parceldelivery.validation.platenumber;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PlateNumberValidator implements ConstraintValidator<PlateNumber, String> {
    private PlateRegion region;
    private final PlateStrategyRegistry plateStrategyRegistry;

    @Override
    public void initialize(PlateNumber constraintAnnotation) {
        this.region = constraintAnnotation.region();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null) {
            return true;
        }
        return plateStrategyRegistry.get(region).isValid(s);
    }
}
