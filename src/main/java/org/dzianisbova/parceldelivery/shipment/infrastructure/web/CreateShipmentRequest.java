package org.dzianisbova.parceldelivery.shipment.infrastructure.web;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
class CreateShipmentRequest {
    @Valid
    @NotNull
    private AddressDto pickupAddress;

    @NotBlank
    private String recipient;

    @Valid
    @NotNull
    private AddressDto deliveryAddress;

    @Valid
    @NotNull
    private ParcelDto parcel;
}
