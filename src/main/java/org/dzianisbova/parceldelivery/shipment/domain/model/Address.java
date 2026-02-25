package org.dzianisbova.parceldelivery.shipment.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Address {
    private String street;
    private String building;
    private String apartment;
    private String city;
    private String postalCode;
    private String country;
}
