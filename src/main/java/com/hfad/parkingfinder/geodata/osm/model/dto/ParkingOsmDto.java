package com.hfad.parkingfinder.geodata.osm.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.var;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParkingOsmDto {
    @JsonProperty("osm_id")
    private Long parkingNodeId;
    @JsonProperty("lat")
    private Double attitude;
    @JsonProperty("lon")
    private Double longitude;
    @JsonProperty("display_name")
    private String displayName;
    private AddressOsmDto address;
    @JsonProperty("extratags")
    private ParkingDetailsOsmDto parkingDetails;

    public String getFullAddress() {
        var fullAddress = "";
        if (address != null) {
            if (address.getRoad() != null) {
                val part1 = address.getRoad().replace(",", "").replace("null", "").trim();
                if (!part1.isEmpty()) {
                    fullAddress += part1;
                }
            }
            if (address.getTown() != null) {
                val part2 = address.getTown().replace(",", "").replace("null", "").trim();
                if (!part2.isEmpty()) {
                    if (!fullAddress.isEmpty()) fullAddress += ", ";
                    fullAddress += part2;
                }
            }
            if (address.getPostCode() != null) {
                String part3 = "";
                if (address.getPostCode().contains(" ")) {
                    part3 = address.getPostCode().substring(0, address.getPostCode().indexOf(" ")).replace(",", "").replace("null", "").trim();
                }
                if (!part3.isEmpty()) {
                    if (!fullAddress.isEmpty()) fullAddress += ", ";
                    fullAddress += part3;
                }
            }
            if (displayName != null) {
                String part4 = "";
                if (displayName.contains(",")) {
                    part4 = displayName.substring(displayName.indexOf(",")).replace(",", "").replace("null", "").trim();
                }
                if (!part4.isEmpty()) {
                    if (!fullAddress.isEmpty()) fullAddress += ", ";
                    fullAddress += part4;
                }
            }
        }
        return fullAddress;
    }
}
