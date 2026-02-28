package org.dzianisbova.parceldelivery.shipment.infrastructure.persistence.shipment;

import org.dzianisbova.parceldelivery.shipment.domain.port.TrackingIdSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
class JpaTrackingIdSource implements TrackingIdSource {
    private final JdbcTemplate jdbcTemplate;

    JpaTrackingIdSource(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public long next() {
        return jdbcTemplate.queryForObject("SELECT nextval('tracking_number_seq')", Long.class);
    }
}
