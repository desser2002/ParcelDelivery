package org.dzianisbova.parceldelivery.validation.platenumber.strategys;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class PolandPlateValidationStrategyTest {
    private final PolandPlateValidationStrategy strategy = new PolandPlateValidationStrategy();

    @ParameterizedTest(name = "{0} should be valid plateNumber")
    @ValueSource(strings = {
        "WA12345",
        "SG12345",
        "KR1234A",
        "SBE12345"
    })
    void isValid_forValidPlateNumber_shouldReturnTrue(String plateNumber) {
        assertThat(strategy.isValid(plateNumber)).isTrue();
    }

    @ParameterizedTest(name = "{1}")
    @MethodSource("invalidPlatesNumbers")
    void isValid_forValidPlateNumber_shouldReturnTrue(String plateNumber, String reason) {
        assertThat(strategy.isValid(plateNumber)).isFalse();
    }

    static Stream<Arguments> invalidPlatesNumbers() {
        return Stream.of(
            Arguments.of("wa12345", "lowercase letters"),
            Arguments.of("W123", "prefix too short"),
            Arguments.of("WA1234I", "forbidden letter I"),
            Arguments.of("WA1234B", "forbidden letter B"),
            Arguments.of("WA1234D", "forbidden letter D"),
            Arguments.of("", "empty string"),
            Arguments.of("WA", "only prefix"),
            Arguments.of("12345", "only digits"),
            Arguments.of("WA12 345", "space inside"),
            Arguments.of("WA-12345", "dash inside")
        );
    }

    @Test
    void isValid_forNullPlateNumber_shouldReturnFalse() {
        assertThat(strategy.isValid(null)).isFalse();
    }

    @Test
    void isValid_forBlankPlateNumber_shouldReturnFalse() {
        assertThat(strategy.isValid("")).isFalse();
    }
}