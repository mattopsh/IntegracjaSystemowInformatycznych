package com.hfad.parkingfinder.geodata.osm.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProviderOsmDto {
    @JsonProperty("id")
    private Long providerNodeId;
    @JsonProperty("lat")
    private Double attitude;
    @JsonProperty("lon")
    private Double longitude;
    @JsonProperty("tags")
    private ProviderOsmDetailsDto providerOsmDetailsDto;
}
