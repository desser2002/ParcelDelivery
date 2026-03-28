package org.dzianisbova.parceldelivery.shipment.infrastructure.web;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.dzianisbova.parceldelivery.shipment.domain.model.Address;

@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
class AddressDto {
    @NotBlank
    private String street;

    @NotBlank
    private String building;

    private String apartment;

    @NotBlank
    private String city;

    private String postalCode;

    @NotBlank
    private String country;

    Address toDomain() {
        return new Address(street, building, apartment, city, postalCode, country);
    }

    static AddressDto from(Address address) {
        return new AddressDto(
                address.getStreet(),
                address.getBuilding(),
                address.getApartment(),
                address.getCity(),
                address.getPostalCode(),
                address.getCountry()
        );
    }
}
