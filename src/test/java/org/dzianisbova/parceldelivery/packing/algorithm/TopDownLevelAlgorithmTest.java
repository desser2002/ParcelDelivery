package org.dzianisbova.parceldelivery.packing.algorithm;

import org.dzianisbova.parceldelivery.domain.model.Dimensions;
import org.dzianisbova.parceldelivery.domain.model.Parcel;
import org.dzianisbova.parceldelivery.domain.model.Vehicle;
import org.dzianisbova.parceldelivery.packing.domain.algorithm.TopDownLevelAlgorithm;
import org.dzianisbova.parceldelivery.packing.domain.model.ParcelPlacement;
import org.dzianisbova.parceldelivery.packing.domain.model.Position;
import org.dzianisbova.parceldelivery.packing.domain.service.PackingContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TopDownLevelAlgorithmTest {

    private TopDownLevelAlgorithm algorithm;
    private PackingContext context;

    @BeforeEach
    void setUp() {
        algorithm = new TopDownLevelAlgorithm();
        context = new PackingContext(new Vehicle("V-1", new Dimensions(100, 100, 100), 1000.0));
        algorithm.initialize(context);
    }

    @Test
    void findPosition_ReturnsPositionNearCeiling_OnFirstCall() {
        Position result = algorithm.findPosition(parcel(10, 10, 10), context);

        assertNotNull(result);
        assertEquals(90.0, result.z());
    }

    @Test
    void findPosition_ReturnsNull_WhenParcelHeightExceedsVehicle() {
        Position result = algorithm.findPosition(parcel(10, 10, 101), context);

        assertNull(result);
    }

    @Test
    void findPosition_CreatesLevelLower_WhenCurrentLevelFull() {
        place(parcel(100, 100, 10));

        Position result = algorithm.findPosition(parcel(10, 10, 10), context);

        assertNotNull(result);
        assertEquals(80.0, result.z());
    }

    @Nested
    class NewStartHeightBoundary {

        @Test
        void createsLevel_WhenNewStartHeightIsZero() {
            context = new PackingContext(new Vehicle("V-2", new Dimensions(100, 100, 20), 1000.0));
            algorithm.initialize(context);

            place(parcel(100, 100, 10));

            Position result = algorithm.findPosition(parcel(10, 10, 10), context);

            assertNotNull(result);
            assertEquals(0.0, result.z());
        }

        @Test
        void returnsNull_WhenNextLevelWouldGoBelowFloor() {
            context = new PackingContext(new Vehicle("V-3", new Dimensions(100, 100, 15), 1000.0));
            algorithm.initialize(context);

            place(parcel(100, 100, 10));

            Position result = algorithm.findPosition(parcel(10, 10, 10), context);

            assertNull(result);
        }
    }

    @Test
    void notifyPlaced_UpdatesTracker_SoNextPositionIsFound() {
        place(parcel(100, 100, 10));

        Parcel parcelB = parcel(50, 100, 10);
        Position posB = algorithm.findPosition(parcelB, context);
        context.addPlacement(new ParcelPlacement(parcelB, posB));
        algorithm.notifyPlaced(parcelB, posB);

        Position posC = algorithm.findPosition(parcel(50, 100, 10), context);

        assertNotNull(posC);
        assertEquals(80.0, posC.z());
        assertEquals(50.0, posC.x());
    }

    private void place(Parcel parcel) {
        Position pos = algorithm.findPosition(parcel, context);
        assertNotNull(pos, "Expected a valid position during test setup");
        context.addPlacement(new ParcelPlacement(parcel, pos));
        algorithm.notifyPlaced(parcel, pos);
    }

    private Parcel parcel(double length, double width, double height) {
        return new Parcel("P", new Dimensions(length, width, height), 5.0);
    }
}
