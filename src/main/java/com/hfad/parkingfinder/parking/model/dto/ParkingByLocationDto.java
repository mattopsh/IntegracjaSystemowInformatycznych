package com.hfad.parkingfinder.parking.model.dto;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkingByLocationDto {
    private Long parkingNodeId;
    private String parkingName;
    private String parkingAddress;
    private Double attitude;
    private Double longitude;
    private Integer distance;
    private Integer freeSpaces;
}

