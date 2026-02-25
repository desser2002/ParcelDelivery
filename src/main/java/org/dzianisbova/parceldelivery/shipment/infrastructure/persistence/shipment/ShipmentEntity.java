package org.dzianisbova.parceldelivery.shipment.infrastructure.persistence.shipment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "shipments")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
public class ShipmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(name = "tracking_number", unique = true, nullable = false, length = 20)
    private String trackingNumber;

    @Column(name = "sender_id")
    private UUID senderId;

    @Column(name = "pickup_street", nullable = false)
    private String pickupStreet;

    @Column(name = "pickup_building", nullable = false)
    private String pickupBuilding;

    @Column(name = "pickup_apartment")
    private String pickupApartment;

    @Column(name = "pickup_city", nullable = false)
    private String pickupCity;

    @Column(name = "pickup_postal_code")
    private String pickupPostalCode;

    @Column(name = "pickup_country", nullable = false, length = 2)
    private String pickupCountry;

    @Column(name = "recipient_name", nullable = false)
    private String recipient;

    @Column(name = "delivery_street", nullable = false)
    private String deliveryStreet;

    @Column(name = "delivery_building", nullable = false)
    private String deliveryBuilding;

    @Column(name = "delivery_apartment")
    private String deliveryApartment;

    @Column(name = "delivery_city", nullable = false)
    private String deliveryCity;

    @Column(name = "delivery_postal_code")
    private String deliveryPostalCode;

    @Column(name = "delivery_country", nullable = false, length = 2)
    private String deliveryCountry;

    @Column(nullable = false)
    private double length;

    @Column(nullable = false)
    private double width;

    @Column(nullable = false)
    private double height;

    @Column(nullable = false)
    private double weight;

    @Column(nullable = false)
    private boolean fragile;

    @Column(nullable = false, length = 20)
    private String priority;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
