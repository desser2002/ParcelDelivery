package org.dzianisbova.parceldelivery.shipment.infrastructure.tracking;

import org.dzianisbova.parceldelivery.shipment.infrastructure.persistence.shipment.ShipmentJpaRepository;
import org.hashids.Hashids;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HashidsTrackingNumberGeneratorTest {

    private static final String SALT = "test-salt";
    private static final int MIN_LENGTH = 8;

    private ShipmentJpaRepository repository;
    private HashidsTrackingNumberGenerator generator;

    @BeforeEach
    void setUp() {
        repository = mock(ShipmentJpaRepository.class);
        generator = new HashidsTrackingNumberGenerator(repository, SALT, MIN_LENGTH);
    }

    @Test
    void generate_encodesSequenceValueWithHashids() {
        when(repository.nextTrackingNumber()).thenReturn(42L);

        String result = generator.generate();

        assertEquals(new Hashids(SALT, MIN_LENGTH, "0123456789ABCDEF").encode(42L), result);
    }

    @Test
    void generate_respectsMinLength() {
        when(repository.nextTrackingNumber()).thenReturn(1L);

        String result = generator.generate();

        assertTrue(result.length() >= MIN_LENGTH);
    }

    @Test
    void generate_hashidsOutputIsStableAcrossLibraryVersions() {
        when(repository.nextTrackingNumber()).thenReturn(1L);

        String result = generator.generate();
        assertEquals("E298E283", result);
    }
}
