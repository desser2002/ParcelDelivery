package org.dzianisbova.parceldelivery.packing.algorithm;

import org.dzianisbova.parceldelivery.domain.model.Dimensions;
import org.dzianisbova.parceldelivery.domain.model.Parcel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LevelTest {
    private static final Dimensions CONTAINER_100x100x100 = new Dimensions(100, 100, 100);
    private static final double DEFAULT_WEIGHT = 5.0;

    private Level level;

    @BeforeEach
    void setUp() {
        level = new Level(0, CONTAINER_100x100x100);
    }

    @Nested
    @DisplayName("canFit()")
    class CanFit {
        @Test
        @DisplayName("returns true when level is empty")
        void returnsTrue_WhenLevelIsEmpty() {
            Parcel parcel = parcel(10, 10, 10);

            boolean result = level.canFit(parcel);

            assertTrue(result);
        }

        @Test
        @DisplayName("returns false when parcel exceeds container bounds")
        void returnsFalse_WhenParcelExceedsBounds() {
            Parcel parcel = parcel(101, 101, 101);

            boolean result = level.canFit(parcel);

            assertFalse(result);
        }

        @Test
        @DisplayName("returns false when level is fully occupied")
        void returnsFalse_WhenLevelFullyOccupied() {
            Parcel fullWidthParcel = parcel(100, 100, 95);
            level.placeParcel(fullWidthParcel);
            Parcel anotherParcel = parcel(10, 10, 10);

            boolean result = level.canFit(anotherParcel);

            assertFalse(result);
        }
    }

    @Nested
    @DisplayName("placeParcel()")
    class PlaceParcel {
        @Test
        @DisplayName("adds parcel to placements list")
        void addsParcel_ToPlacements() {
            Parcel parcel = parcel(10, 10, 10);

            level.placeParcel(parcel);

            assertEquals(1, level.getPlacements().size());
        }

        @Test
        @DisplayName("throws IllegalStateException when parcel does not fit")
        void throwsException_WhenParcelDoesNotFit() {
            Parcel parcel = parcel(101, 101, 101);

            assertThrows(IllegalStateException.class, () -> level.placeParcel(parcel));
        }

        @Test
        @DisplayName("updates level height to parcel height")
        void updatesHeight_ToParcelHeight() {
            Parcel parcel = parcel(10, 10, 20);

            level.placeParcel(parcel);

            assertEquals(20, level.getHeight());
        }

        @Test
        @DisplayName("updates level height to tallest parcel")
        void updatesHeight_ToTallestParcel() {
            level.placeParcel(parcel(10, 10, 10));
            level.placeParcel(parcel(10, 10, 25));

            assertEquals(25, level.getHeight());
        }
    }

    @Nested
    @DisplayName("getTopZ()")
    class GetTopZ {
        @Test
        @DisplayName("returns start height plus level height")
        void returnsSum_OfStartHeightAndLevelHeight() {
            double startHeight = 15;
            double parcelHeight = 20;
            Level levelAtHeight = new Level(startHeight, CONTAINER_100x100x100);

            levelAtHeight.placeParcel(parcel(10, 10, parcelHeight));

            assertEquals(35, levelAtHeight.getTopZ());
        }

        @Test
        @DisplayName("returns start height when level is empty")
        void returnsStartHeight_WhenLevelEmpty() {
            double startHeight = 10;
            Level levelAtHeight = new Level(startHeight, CONTAINER_100x100x100);

            assertEquals(10, levelAtHeight.getTopZ());
        }
    }

    private Parcel parcel(double length, double width, double height) {
        return new Parcel("P-test", new Dimensions(length, width, height), DEFAULT_WEIGHT);
    }
}