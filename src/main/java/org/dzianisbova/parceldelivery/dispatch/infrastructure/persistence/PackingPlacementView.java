package org.dzianisbova.parceldelivery.dispatch.infrastructure.persistence;

import java.util.UUID;

interface PackingPlacementView {
    double getPosX();

    double getPosY();

    double getPosZ();

    UUID getParcelId();

    double getLength();

    double getWidth();

    double getHeight();

    double getWeight();

    boolean isFragile();

    String getPriority();
}
