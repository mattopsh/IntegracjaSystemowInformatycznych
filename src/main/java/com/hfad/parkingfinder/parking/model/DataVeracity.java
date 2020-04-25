package com.hfad.parkingfinder.parking.model;

public enum DataVeracity {
    LOW(30),
    MEDIUM(60),
    HIGH(90);

    DataVeracity(int minDataVeracity) {
        this.minDataVeracity = minDataVeracity;
    }

    private final int minDataVeracity;

    public int getMinDataVeracity() {
        return minDataVeracity;
    }
}
