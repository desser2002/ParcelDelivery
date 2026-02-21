package org.dzianisbova.parceldelivery.packing.policy;

import org.dzianisbova.parceldelivery.domain.model.Dimensions;
import org.dzianisbova.parceldelivery.domain.model.Parcel;
import org.dzianisbova.parceldelivery.domain.model.Priority;
import org.dzianisbova.parceldelivery.domain.model.Vehicle;
import org.dzianisbova.parceldelivery.packing.domain.model.ParcelPlacement;
import org.dzianisbova.parceldelivery.packing.domain.model.Position;
import org.dzianisbova.parceldelivery.packing.domain.policy.FragilePolicy;
import org.dzianisbova.parceldelivery.packing.domain.service.PackingContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FragilePolicyTest {

    private static final Dimensions DIM_10x10x10 = new Dimensions(10, 10, 10);

    private FragilePolicy policy;
    private PackingContext context;

    @BeforeEach
    void setUp() {
        policy = new FragilePolicy();
        context = new PackingContext(new Vehicle("V-1", new Dimensions(100, 100, 100), 1000));
    }

    @Test
    void allowsStandardParcel_WhenContextIsEmpty() {
        Parcel parcel = standardParcel();

        assertTrue(policy.canPlace(parcel, new Position(0, 0, 10), context));
    }

    @Test
    void rejectsStandardParcel_WhenFragileParcelIsBelow() {
        context.addPlacement(new ParcelPlacement(fragileParcel(), new Position(0, 0, 0)));

        Parcel parcel = standardParcel();

        assertFalse(policy.canPlace(parcel, new Position(0, 0, 10), context));
    }

    @Test
    void allowsStandardParcel_WhenParcelsExistAbove() {
        context.addPlacement(new ParcelPlacement(standardParcel(), new Position(0, 0, 20)));

        Parcel parcel = standardParcel();

        assertTrue(policy.canPlace(parcel, new Position(0, 0, 10), context));
    }

    @Test
    void allowsFragileParcel_WhenContextIsEmpty() {
        Parcel parcel = fragileParcel();

        assertTrue(policy.canPlace(parcel, new Position(0, 0, 0), context));
    }

    @Test
    void rejectsFragileParcel_WhenParcelsExistAbove() {
        context.addPlacement(new ParcelPlacement(standardParcel(), new Position(0, 0, 20)));

        Parcel parcel = fragileParcel();

        assertFalse(policy.canPlace(parcel, new Position(0, 0, 10), context));
    }

    @Test
    void rejectsFragileParcel_WhenFragileParcelIsBelow() {
        context.addPlacement(new ParcelPlacement(fragileParcel(), new Position(0, 0, 0)));

        Parcel parcel = fragileParcel();

        assertFalse(policy.canPlace(parcel, new Position(0, 0, 10), context));
    }

    private Parcel standardParcel() {
        return new Parcel("P-std", DIM_10x10x10, 5.0, false, Priority.STANDARD);
    }

    private Parcel fragileParcel() {
        return new Parcel("P-fr", DIM_10x10x10, 5.0, true, Priority.STANDARD);
    }
}
