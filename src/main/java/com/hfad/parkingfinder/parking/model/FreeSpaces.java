package com.hfad.parkingfinder.parking.model;

public enum FreeSpaces {
    LOW(5),
    MEDIUM(15),
    HIGH(30);

    FreeSpaces(int minFreeSpaces){
        this.minFreeSpaces = minFreeSpaces;
    }

    private final int minFreeSpaces;

    public int getMinFreeSpaces() {
        return minFreeSpaces;
    }
}
