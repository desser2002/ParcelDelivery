package org.dzianisbova.parceldelivery.packing.algorithm;

import org.dzianisbova.parceldelivery.domain.model.Dimensions;
import org.dzianisbova.parceldelivery.domain.model.Parcel;
import org.dzianisbova.parceldelivery.packing.domain.algorithm.ExtremePointTracker;
import org.dzianisbova.parceldelivery.packing.domain.model.ParcelPlacement;
import org.dzianisbova.parceldelivery.packing.domain.model.Position;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExtremePointTrackerTest {

    private static final Dimensions CONTAINER_100x100x100 = new Dimensions(100, 100, 100);

    @Test
    void startsWithSinglePoint_AtStartHeight() {
        ExtremePointTracker tracker = new ExtremePointTracker(15, CONTAINER_100x100x100);

        List<Position> points = tracker.getPoints();

        assertEquals(1, points.size());
        assertEquals(new Position(0, 0, 15), points.getFirst());
    }

    @Test
    void afterUpdate_UsedPointRemoved_TwoNewPointsAdded() {
        ExtremePointTracker tracker = new ExtremePointTracker(0, CONTAINER_100x100x100);

        tracker.update(placement(new Position(0, 0, 0), new Dimensions(20, 30, 10)));

        List<Position> points = tracker.getPoints();
        assertFalse(points.contains(new Position(0, 0, 0)), "used point must be removed");
        assertTrue(points.contains(new Position(20, 0, 0)), "x-extreme must be added");
        assertTrue(points.contains(new Position(0, 30, 0)), "y-extreme must be added");
        assertEquals(2, points.size());
    }

    @Test
    void extremePoint_NotAdded_WhenReachesContainerBoundary() {
        ExtremePointTracker tracker = new ExtremePointTracker(0, CONTAINER_100x100x100);

        tracker.update(placement(new Position(0, 0, 0), new Dimensions(100, 50, 10)));

        List<Position> points = tracker.getPoints();
        assertTrue(points.stream().noneMatch(p -> p.x() == 100), "x-extreme at boundary must not be added");
        assertTrue(points.contains(new Position(0, 50, 0)), "y-extreme within bounds must be added");
        assertEquals(1, points.size());
    }

    @Test
    void stalePoint_InsideNewParcel_IsRemoved() {
        ExtremePointTracker tracker = new ExtremePointTracker(0, CONTAINER_100x100x100);

        tracker.update(placement(new Position(0, 0, 0), new Dimensions(20, 100, 10)));

        tracker.update(placement(new Position(10, 0, 0), new Dimensions(15, 100, 10)));

        List<Position> points = tracker.getPoints();
        assertFalse(points.contains(new Position(20, 0, 0)), "stale point inside parcel B must be removed");
        assertTrue(points.contains(new Position(25, 0, 0)), "x-extreme of parcel B must be added");
    }

    @Test
    void point_AtExactParcelBoundary_IsNotRemoved() {
        ExtremePointTracker tracker = new ExtremePointTracker(0, CONTAINER_100x100x100);

        tracker.update(placement(new Position(0, 0, 0), new Dimensions(25, 100, 10)));

        tracker.update(placement(new Position(10, 0, 0), new Dimensions(15, 100, 10)));

        List<Position> points = tracker.getPoints();
        assertTrue(points.contains(new Position(25, 0, 0)), "point at exact parcel boundary must not be removed");
    }

    private ParcelPlacement placement(Position position, Dimensions dimensions) {
        Parcel parcel = new Parcel("P-test", dimensions, 5.0);
        return new ParcelPlacement(parcel, position);
    }
}
