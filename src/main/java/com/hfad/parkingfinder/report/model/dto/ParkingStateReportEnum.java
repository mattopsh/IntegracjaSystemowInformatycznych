package com.hfad.parkingfinder.report.model.dto;

public enum ParkingStateReportEnum {
    ABOUT_0(0),
    ABOVE_5(5),
    ABOVE_15(15),
    ABOVE_30(30);

    ParkingStateReportEnum(int freeSpaces){
        this.freeSpaces = freeSpaces;
    }

    private final Integer freeSpaces;

    public Integer getFreeSpaces() {
        return freeSpaces;
    }
}
