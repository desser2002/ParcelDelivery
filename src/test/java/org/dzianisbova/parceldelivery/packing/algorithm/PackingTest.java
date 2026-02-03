package org.dzianisbova.parceldelivery.packing.algorithm;

import org.dzianisbova.parceldelivery.domain.model.Dimensions;
import org.dzianisbova.parceldelivery.domain.model.Parcel;
import org.dzianisbova.parceldelivery.domain.model.Vehicle;
import org.dzianisbova.parceldelivery.packing.model.VehiclePackingResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PackingTest {
    private static final Dimensions VEHICLE_DIMENSIONS = new Dimensions(100, 100, 100);
    private static final double VEHICLE_MAX_WEIGHT = 1000;

    private Packing packing;
    private Vehicle vehicle;

    @BeforeEach
    void setUp() {
        packing = new Packing();
        vehicle = new Vehicle("VAN-001", VEHICLE_DIMENSIONS, VEHICLE_MAX_WEIGHT);
    }

    @Test
    @DisplayName("Should return empty result when no parcels provided")
    void pack_WhenParcelListIsEmpty_ShouldReturnEmptyResult() {
        List<Parcel> parcels = List.of();

        VehiclePackingResult result = packing.pack(parcels, vehicle);

        assertTrue(result.getPlacements().isEmpty());
    }

    @Test
    @DisplayName("Should pack parcel when it fits in vehicle")
    void pack_WhenSingleParcelFitsInVehicle_ShouldPackIt() {
        Parcel parcel = new Parcel("P1", new Dimensions(10, 10, 10), 5);

        VehiclePackingResult result = packing.pack(List.of(parcel), vehicle);

        assertEquals(1, result.getPlacements().size());
    }

    @Test
    @DisplayName("Should pack all parcels when they all fit in vehicle")
    void pack_WhenAllParcelsHaveEnoughSpaceAndWeight_ShouldPackAll() {
        List<Parcel> parcels = List.of(
                new Parcel("P1", new Dimensions(20, 20, 20), 5),
                new Parcel("P2", new Dimensions(20, 20, 20), 5),
                new Parcel("P3", new Dimensions(20, 20, 20), 5)
        );

        VehiclePackingResult result = packing.pack(parcels, vehicle);

        assertEquals(3, result.getPlacements().size());
    }

    @Test
    @DisplayName("Should not pack parcel when it exceeds max weight")
    void pack_WhenParcelWeightExceedsMaxWeight_ShouldNotPackIt() {
        Parcel heavyParcel = new Parcel("P1", new Dimensions(10, 10, 10), 1001);

        VehiclePackingResult result = packing.pack(List.of(heavyParcel), vehicle);

        assertTrue(result.getPlacements().isEmpty());
    }

    @Test
    @DisplayName("Should pack parcel when its weight is exactly max weight")
    void pack_WhenParcelWeightIsExactlyMaxWeight_ShouldPackIt() {
        Parcel parcel = new Parcel("P1", new Dimensions(10, 10, 10), VEHICLE_MAX_WEIGHT);

        VehiclePackingResult result = packing.pack(List.of(parcel), vehicle);

        assertEquals(1, result.getPlacements().size());
    }
}