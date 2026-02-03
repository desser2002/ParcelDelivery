package org.dzianisbova.parceldelivery.packing.model;

import org.dzianisbova.parceldelivery.domain.model.Dimensions;
import org.dzianisbova.parceldelivery.domain.model.Parcel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("ParcelPlacement Collision Detection")
class ParcelPlacementTest {
    private static final Dimensions STANDARD_DIMENSIONS = new Dimensions(20, 20, 20);
    private static final Position ORIGIN = new Position(0, 0, 0);

    @Test
    @DisplayName("Should detect collision when parcels overlap")
    void collidesWith_WhenParcelsOverlap_ShouldReturnTrue() {
        ParcelPlacement placement = createPlacement(new Position(10, 10, 10));

        boolean collides = placement.collidesWith(new Position(15, 15, 15), STANDARD_DIMENSIONS);

        assertTrue(collides);
    }

    @Test
    @DisplayName("Should not detect collision when parcels separated on X axis")
    void collidesWith_WhenSeparatedOnX_ShouldReturnFalse() {
        ParcelPlacement placement = createPlacement(ORIGIN);

        boolean collides = placement.collidesWith(new Position(20, 0, 0), STANDARD_DIMENSIONS);

        assertFalse(collides);
    }

    @Test
    @DisplayName("Should not detect collision when parcels separated on Y axis")
    void collidesWith_WhenSeparatedOnY_ShouldReturnFalse() {
        ParcelPlacement placement = createPlacement(ORIGIN);

        boolean collides = placement.collidesWith(new Position(0, 20, 0), STANDARD_DIMENSIONS);

        assertFalse(collides);
    }

    @Test
    @DisplayName("Should not detect collision when parcels separated on Z axis")
    void collidesWith_WhenSeparatedOnZ_ShouldReturnFalse() {
        ParcelPlacement placement = createPlacement(ORIGIN);

        boolean collides = placement.collidesWith(new Position(0, 0, 20), STANDARD_DIMENSIONS);

        assertFalse(collides);
    }

    @Test
    @DisplayName("Should not detect collision when parcels far apart")
    void collidesWith_WhenFarApart_ShouldReturnFalse() {
        ParcelPlacement placement = createPlacement(ORIGIN);

        boolean collides = placement.collidesWith(new Position(100, 100, 100), STANDARD_DIMENSIONS);

        assertFalse(collides);
    }

    private ParcelPlacement createPlacement(Position position) {
        Parcel parcel = new Parcel("P1", STANDARD_DIMENSIONS, 5);
        return new ParcelPlacement(parcel, position);
    }
}