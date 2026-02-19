package org.dzianisbova.parceldelivery.packing.algorithm;

import org.dzianisbova.parceldelivery.domain.model.Dimensions;
import org.dzianisbova.parceldelivery.domain.model.Parcel;
import org.dzianisbova.parceldelivery.packing.domain.algorithm.Level;
import org.dzianisbova.parceldelivery.packing.domain.model.Position;
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
    @DisplayName("findLowestAvailablePosition()")
    class FindLowestAvailablePosition {
        @Test
        @DisplayName("returns position when level is empty")
        void returnsPosition_WhenLevelIsEmpty() {
            Parcel parcel = parcel(10, 10, 10);

            Position result = level.findLowestAvailablePosition(parcel);

            assertNotNull(result);
        }

        @Test
        @DisplayName("returns null when parcel exceeds container bounds")
        void returnsNull_WhenParcelExceedsBounds() {
            Parcel parcel = parcel(101, 101, 101);

            Position result = level.findLowestAvailablePosition(parcel);

            assertNull(result);
        }

        @Test
        @DisplayName("returns null when level is fully occupied")
        void returnsNull_WhenLevelFullyOccupied() {
            place(level, parcel(100, 100, 95));
            Parcel anotherParcel = parcel(10, 10, 10);

            Position result = level.findLowestAvailablePosition(anotherParcel);

            assertNull(result);
        }
    }

    @Nested
    @DisplayName("placeParcelAt()")
    class PlaceParcelAt {
        @Test
        @DisplayName("adds parcel to placements list")
        void addsParcel_ToPlacements() {
            Parcel parcel = parcel(10, 10, 10);

            place(level, parcel);

            assertEquals(1, level.getPlacements().size());
        }

        @Test
        @DisplayName("throws IllegalStateException when parcel does not fit at position")
        void throwsException_WhenParcelDoesNotFit() {
            Parcel parcel = parcel(101, 101, 101);
            Position outOfBounds = new Position(0, 0, 0);

            assertThrows(IllegalStateException.class, () -> level.placeParcelAt(parcel, outOfBounds));
        }

        @Test
        @DisplayName("updates maxPlacedHeight to parcel height")
        void updatesMaxPlacedHeight_ToParcelHeight() {
            Parcel parcel = parcel(10, 10, 20);

            place(level, parcel);

            assertEquals(20, level.getMaxPlacedHeight());
        }

        @Test
        @DisplayName("updates maxPlacedHeight to tallest parcel")
        void updatesMaxPlacedHeight_ToTallestParcel() {
            place(level, parcel(10, 10, 10));
            place(level, parcel(10, 10, 25));

            assertEquals(25, level.getMaxPlacedHeight());
        }
    }

    @Nested
    @DisplayName("getTopZ()")
    class GetTopZ {
        @Test
        @DisplayName("returns start height plus max placed height")
        void returnsSum_OfStartHeightAndMaxPlacedHeight() {
            double startHeight = 15;
            double parcelHeight = 20;
            Level levelAtHeight = new Level(startHeight, CONTAINER_100x100x100);

            place(levelAtHeight, parcel(10, 10, parcelHeight));

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

    private void place(Level level, Parcel parcel) {
        Position pos = level.findLowestAvailablePosition(parcel);
        assertNotNull(pos, "Expected a valid position for parcel " + parcel.getId());
        level.placeParcelAt(parcel, pos);
    }

    private Parcel parcel(double length, double width, double height) {
        return new Parcel("P-test", new Dimensions(length, width, height), DEFAULT_WEIGHT);
    }
}
