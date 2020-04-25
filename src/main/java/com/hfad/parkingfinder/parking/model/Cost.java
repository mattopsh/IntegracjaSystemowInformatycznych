package com.hfad.parkingfinder.parking.model;

public enum Cost {
    PAID,
    LIMITED,
    FREE;

    public static Cost fromOsmString(String fee) {
        if (fee == null) {
            return null;
        } else if (fee.toLowerCase().equals("no")) {
            return Cost.FREE;
        } else if (fee.toLowerCase().equals("yes")) {
            return Cost.PAID;
        } else {
            return Cost.LIMITED;
        }
    }
}
