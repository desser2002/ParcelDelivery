package org.dzianisbova.parceldelivery.shipment.infrastructure.persistence.shipment;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "shipments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ShipmentEntity {
    @Id
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(name = "tracking_number", unique = true, nullable = false, length = 20)
    private String trackingNumber;

    @Column(name = "sender_id")
    private UUID senderId;

    @Embedded
    @AttributeOverride(name = "street", column = @Column(name = "pickup_street", nullable = false))
    @AttributeOverride(name = "building", column = @Column(name = "pickup_building", nullable = false))
    @AttributeOverride(name = "apartment", column = @Column(name = "pickup_apartment"))
    @AttributeOverride(name = "city", column = @Column(name = "pickup_city", nullable = false))
    @AttributeOverride(name = "postalCode", column = @Column(name = "pickup_postal_code"))
    @AttributeOverride(name = "country", column = @Column(name = "pickup_country", nullable = false, length = 2))
    private AddressEmbeddable pickup;

    @Column(name = "recipient_name", nullable = false)
    private String recipient;

    @Embedded
    @AttributeOverride(name = "street", column = @Column(name = "delivery_street", nullable = false))
    @AttributeOverride(name = "building", column = @Column(name = "delivery_building", nullable = false))
    @AttributeOverride(name = "apartment", column = @Column(name = "delivery_apartment"))
    @AttributeOverride(name = "city", column = @Column(name = "delivery_city", nullable = false))
    @AttributeOverride(name = "postalCode", column = @Column(name = "delivery_postal_code"))
    @AttributeOverride(name = "country", column = @Column(name = "delivery_country", nullable = false, length = 2))
    private AddressEmbeddable delivery;

    @Embedded
    private ParcelEmbeddable parcel;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "vehicle_id")
    private UUID vehicleId;

    @Column(name = "sorting_center_id")
    private UUID sortingCenterId;
}
