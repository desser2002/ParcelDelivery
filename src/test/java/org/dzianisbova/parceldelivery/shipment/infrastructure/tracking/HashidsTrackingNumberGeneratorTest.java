package org.dzianisbova.parceldelivery.shipment.infrastructure.tracking;

import org.dzianisbova.parceldelivery.shipment.domain.port.TrackingIdSource;
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

    private TrackingIdSource trackingIdSource;
    private HashidsTrackingNumberGenerator generator;

    @BeforeEach
    void setUp() {
        trackingIdSource = mock(TrackingIdSource.class);
        generator = new HashidsTrackingNumberGenerator(trackingIdSource, SALT, MIN_LENGTH);
    }

    @Test
    void generate_encodesSequenceValueWithHashids() {
        when(trackingIdSource.next()).thenReturn(42L);

        String result = generator.generate();

        assertEquals(new Hashids(SALT, MIN_LENGTH, "0123456789ABCDEF").encode(42L), result);
    }

    @Test
    void generate_respectsMinLength() {
        when(trackingIdSource.next()).thenReturn(1L);

        String result = generator.generate();

        assertTrue(result.length() >= MIN_LENGTH);
    }

    @Test
    void generate_hashidsOutputIsStableAcrossLibraryVersions() {
        when(trackingIdSource.next()).thenReturn(1L);

        String result = generator.generate();
        assertEquals("E298E283", result);
    }
}
