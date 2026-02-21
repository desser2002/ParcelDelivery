package org.dzianisbova.parceldelivery.packing.service;

import org.dzianisbova.parceldelivery.domain.model.Dimensions;
import org.dzianisbova.parceldelivery.domain.model.Parcel;
import org.dzianisbova.parceldelivery.domain.model.Priority;
import org.dzianisbova.parceldelivery.domain.model.Vehicle;
import org.dzianisbova.parceldelivery.packing.domain.model.ParcelPlacement;
import org.dzianisbova.parceldelivery.packing.domain.model.Position;
import org.dzianisbova.parceldelivery.packing.domain.service.PackingContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PackingContextTest {

    private static final Dimensions VEHICLE_DIM = new Dimensions(100, 100, 100);
    private static final double MAX_WEIGHT = 100.0;

    private PackingContext context;

    @BeforeEach
    void setUp() {
        Vehicle vehicle = new Vehicle("V-1", VEHICLE_DIM, MAX_WEIGHT);
        context = new PackingContext(vehicle);
    }

    @Nested
    class ExceedsWeightLimit {

        @Test
        void returnsFalse_WhenWeightExactlyAtLimit() {
            context.addPlacement(placement(parcel(60.0), Position.origin()));

            assertFalse(context.exceedsWeightLimit(40.0));
        }

        @Test
        void returnsTrue_WhenWeightExceedsLimit() {
            context.addPlacement(placement(parcel(60.0), Position.origin()));

            assertTrue(context.exceedsWeightLimit(40.1));
        }

        @Test
        void accumulatesWeight_AcrossMultiplePlacements() {
            context.addPlacement(placement(parcel(30.0), new Position(0, 0, 0)));
            context.addPlacement(placement(parcel(30.0), new Position(10, 0, 0)));
            context.addPlacement(placement(parcel(30.0), new Position(20, 0, 0)));

            assertTrue(context.exceedsWeightLimit(10.1));
            assertFalse(context.exceedsWeightLimit(10.0));
        }
    }

    @Nested
    class GetParcelsAbove {

        @Test
        void includesParcel_WhenStartsExactlyAtTopZ() {
            context.addPlacement(placement(parcel(5.0), new Position(0, 0, 10)));
            Position queryPos = new Position(0, 0, 0);
            Dimensions queryDim = new Dimensions(10, 10, 10);

            List<ParcelPlacement> above = context.getParcelsAbove(queryPos, queryDim);

            assertEquals(1, above.size());
        }

        @Test
        void excludesParcel_WhenAboveButNoXYOverlap() {
            context.addPlacement(placement(parcel(5.0), new Position(50, 0, 20)));
            Position queryPos = new Position(0, 0, 0);
            Dimensions queryDim = new Dimensions(10, 10, 10);

            List<ParcelPlacement> above = context.getParcelsAbove(queryPos, queryDim);

            assertTrue(above.isEmpty());
        }

        @Test
        void excludesParcel_WhenBelowWithXYOverlap() {
            context.addPlacement(placement(parcel(5.0), new Position(0, 0, 0)));
            Position queryPos = new Position(0, 0, 10);
            Dimensions queryDim = new Dimensions(10, 10, 10);

            List<ParcelPlacement> above = context.getParcelsAbove(queryPos, queryDim);

            assertTrue(above.isEmpty());
        }

        @Test
        void returnsOnly_ParcelsAboveWithXYOverlap_WhenMultiplePlaced() {
            ParcelPlacement aboveWithOverlap = placement(parcel(5.0), new Position(0, 0, 10));
            ParcelPlacement aboveNoOverlap = placement(parcel(5.0), new Position(50, 0, 20));
            ParcelPlacement below = placement(parcel(5.0), new Position(0, 0, 0));

            context.addPlacement(aboveWithOverlap);
            context.addPlacement(aboveNoOverlap);
            context.addPlacement(below);

            Position queryPos = new Position(0, 0, 0);
            Dimensions queryDim = new Dimensions(10, 10, 10);

            List<ParcelPlacement> above = context.getParcelsAbove(queryPos, queryDim);

            assertEquals(1, above.size());
            assertTrue(above.contains(aboveWithOverlap));
        }
    }

    @Nested
    class GetFragileParcelsBelow {

        @Test
        void excludesParcel_WhenNotFragile() {
            context.addPlacement(placement(standardParcel(5.0), new Position(0, 0, 0)));
            Position queryPos = new Position(0, 0, 20);
            Dimensions queryDim = new Dimensions(10, 10, 10);

            List<ParcelPlacement> below = context.getFragileParcelsBelow(queryPos, queryDim);

            assertTrue(below.isEmpty());
        }

        @Test
        void includesFragileParcel_WhenTopExactlyAtBottomZ() {
            context.addPlacement(placement(fragileParcel(5.0), new Position(0, 0, 0)));
            Position queryPos = new Position(0, 0, 10);
            Dimensions queryDim = new Dimensions(10, 10, 10);

            List<ParcelPlacement> below = context.getFragileParcelsBelow(queryPos, queryDim);

            assertEquals(1, below.size());
        }

        @Test
        void excludesFragileParcel_WhenNoXYOverlap() {
            context.addPlacement(placement(fragileParcel(5.0), new Position(50, 0, 0)));
            Position queryPos = new Position(0, 0, 20);
            Dimensions queryDim = new Dimensions(10, 10, 10);

            List<ParcelPlacement> below = context.getFragileParcelsBelow(queryPos, queryDim);

            assertTrue(below.isEmpty());
        }

        @Test
        void includesFragileParcel_WhenBelowWithXYOverlap() {
            context.addPlacement(placement(fragileParcel(5.0), new Position(0, 0, 0)));
            Position queryPos = new Position(0, 0, 20);
            Dimensions queryDim = new Dimensions(10, 10, 10);

            List<ParcelPlacement> below = context.getFragileParcelsBelow(queryPos, queryDim);

            assertEquals(1, below.size());
        }
    }

    @Nested
    class IsSpaceOccupied {

        @Test
        void returnsTrue_WhenSpaceOccupied() {
            context.addPlacement(placement(parcel(5.0), new Position(0, 0, 0)));

            assertTrue(context.isSpaceOccupied(new Position(0, 0, 0), new Dimensions(10, 10, 10)));
        }

        @Test
        void returnsFalse_WhenSpaceFree() {
            context.addPlacement(placement(parcel(5.0), new Position(0, 0, 0)));

            assertFalse(context.isSpaceOccupied(new Position(50, 50, 50), new Dimensions(10, 10, 10)));
        }
    }

    private ParcelPlacement placement(Parcel parcel, Position position) {
        return new ParcelPlacement(parcel, position);
    }

    private Parcel parcel(double weight) {
        return new Parcel("P-" + weight, new Dimensions(10, 10, 10), weight);
    }

    private Parcel standardParcel(double weight) {
        return new Parcel("P-std-" + weight, new Dimensions(10, 10, 10), weight, false, Priority.STANDARD);
    }

    private Parcel fragileParcel(double weight) {
        return new Parcel("P-fr-" + weight, new Dimensions(10, 10, 10), weight, true, Priority.STANDARD);
    }
}
