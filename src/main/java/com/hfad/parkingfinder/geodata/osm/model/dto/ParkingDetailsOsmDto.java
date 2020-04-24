package com.hfad.parkingfinder.geodata.osm.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParkingDetailsOsmDto {
    private String name;
    private String access;
    @JsonProperty("park_ride")
    private String parkRide;
    private String fee;
    private String supervised;
    private String capacity;
    @JsonProperty("capacity:disabled")
    private String disabledSpaces;
    @JsonProperty("capacity:parent")
    private String spacesForFamilies;
    @JsonProperty("capacity:charging")
    private String chargingStation;
    @JsonProperty("maxstay")
    private String maxStay;
    @JsonProperty("opening_hours")
    private String openingHours;
}