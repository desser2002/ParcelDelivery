package org.dzianisbova.parceldelivery.shipment.infrastructure.persistence.shipment;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.dzianisbova.parceldelivery.shipment.domain.model.Address;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
class AddressEmbeddable {
    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String building;

    private String apartment;

    @Column(nullable = false)
    private String city;

    private String postalCode;

    @Column(nullable = false, length = 2)
    private String country;

    static AddressEmbeddable from(Address a) {
        return new AddressEmbeddable(
            a.getStreet(), a.getBuilding(), a.getApartment(),
            a.getCity(), a.getPostalCode(), a.getCountry()
        );
    }

    Address toDomain() {
        return new Address(street, building, apartment, city, postalCode, country);
    }
}
