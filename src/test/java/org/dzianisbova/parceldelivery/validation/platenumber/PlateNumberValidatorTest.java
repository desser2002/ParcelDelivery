package org.dzianisbova.parceldelivery.validation.platenumber;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlateNumberValidatorTest {
    @InjectMocks
    PlateNumberValidator plateNumberValidator;

    @Mock
    ConstraintValidatorContext context;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    PlateStrategyRegistry registry;

    @Test
    void isValid_nullPlateNumber_returnTrue() {
        assertThat(plateNumberValidator.isValid(null, context)).isTrue();
    }

    @Test
    void isValid_regionIsValidReturnTrue_returnTrue() {
        //given
        when(registry.get(any()).isValid(any())).thenReturn(true);
        //when
        assertThat(plateNumberValidator.isValid("any", context)).isTrue();
    }

    @Test
    void isValid_regionIsValidReturnFalse_returnFalse() {
        //given
        when(registry.get(any()).isValid(any())).thenReturn(false);
        //when
        assertThat(plateNumberValidator.isValid("any", context)).isFalse();
    }
}