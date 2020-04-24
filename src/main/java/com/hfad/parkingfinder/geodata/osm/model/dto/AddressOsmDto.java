package com.hfad.parkingfinder.geodata.osm.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressOsmDto {
    private String road;
    private String town;
    private String postCode;
}