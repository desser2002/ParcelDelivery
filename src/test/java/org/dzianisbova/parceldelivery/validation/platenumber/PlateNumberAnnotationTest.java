package org.dzianisbova.parceldelivery.validation.platenumber;

import jakarta.validation.*;
import org.dzianisbova.parceldelivery.validation.platenumber.strategys.PolandPlateValidationStrategy;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class PlateNumberAnnotationTest {
    private static Validator validator;
    private static ValidatorFactory factory;

    @BeforeAll
    static void setUp() {
        var registry = new PlateStrategyRegistry(List.of(new PolandPlateValidationStrategy()));

        factory = createFactory(registry);

        validator = factory.getValidator();
    }

    static class TestDto {
        @PlateNumber(region = PlateRegion.POLAND)
        String plateNumber;

        public TestDto(String plateNumber) {
            this.plateNumber = plateNumber;
        }
    }

    private static final String VALID_PLATE = "WA12345";
    private static final String INVALID_PLATE = "WAI?12345";

    @Test
    void shouldPassValidation_whenPlateNumberIsValid() {
        TestDto dto = new TestDto(VALID_PLATE);
        Set<ConstraintViolation<TestDto>> violations = validator.validate(dto);

        assertThat(violations).isEmpty();
    }

    @Test
    void shouldFailValidation_whenPlateNumberIsInvalid() {
        TestDto dto = new TestDto(INVALID_PLATE);
        Set<ConstraintViolation<TestDto>> violations = validator.validate(dto);

        assertThat(violations).hasSize(1);
    }

    @AfterAll
    static void tearDown() {
        factory.close();
    }

    private static ValidatorFactory createFactory(PlateStrategyRegistry registry) {
        return Validation.byDefaultProvider().configure()
            .constraintValidatorFactory(new ConstraintValidatorFactory() {
                @Override
                public <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> validatorClass) {
                    if (validatorClass == PlateNumberValidator.class) {
                        return validatorClass.cast(new PlateNumberValidator(registry));
                    }
                    try {
                        return validatorClass.getDeclaredConstructor().newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public void releaseInstance(ConstraintValidator<?, ?> constraintValidator) {
                    // No cleanup needed — validators are stateless and GC will handle them
                }
            }).buildValidatorFactory();
    }
}
