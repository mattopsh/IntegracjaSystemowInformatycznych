package com.hfad.parkingfinder.geodata.osm.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProviderOsmDetailsDto {
    private String name;
    @JsonProperty("opening_hours")
    private String openingHours;
    @JsonProperty("shop")
    private String providerType;
    @JsonProperty("website")
    private String url;
}