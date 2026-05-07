package org.dzianisbova.parceldelivery.shipment.infrastructure.persistence.tracking;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.dzianisbova.parceldelivery.shipment.domain.model.tracking.TrackingEventType;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tracking_events")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TrackingEventEntity {
    @Id
    @Column(nullable = false,updatable = false)
    private UUID id;

    @Column(name = "shipment_id", nullable = false)
    private UUID shipmentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TrackingEventType type;

    @Column(name = "occurred_at", nullable = false)
    private LocalDateTime occurredAt;

    @Column(name = "actor_id")
    private UUID actorId;


    @Column(name = "vehicle_id")
    private UUID vehicleId;
}
