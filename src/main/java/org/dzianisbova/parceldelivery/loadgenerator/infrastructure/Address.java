package org.dzianisbova.parceldelivery.loadgenerator.infrastructure;

record Address(
        String street,
        String building,
        String apartment,
        String city,
        String postalCode,
        String country
) {}
