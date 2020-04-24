package com.hfad.parkingfinder.geodata.osm.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProvidersOsmDto {
    @JsonProperty("elements")
    private List<ProviderOsmDto> providerOsmDto;
}
