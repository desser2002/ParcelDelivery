package org.dzianisbova.parceldelivery.loadgenerator.infrastructure;

record Parcel(
        double length,
        double width,
        double height,
        double weight,
        boolean fragile,
        String priority
) {
}