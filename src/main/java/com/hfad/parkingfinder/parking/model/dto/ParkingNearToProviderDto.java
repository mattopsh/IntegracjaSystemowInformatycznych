package com.hfad.parkingfinder.parking.model.dto;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkingNearToProviderDto {
    private Long parkingNodeId;
    private String parkingName;
    private String parkingAddress;
    private Double attitude;
    private Double longitude;
    private Integer distanceToParking;
    private Integer distanceToProvider;
    private Integer freeSpaces;
    private Integer dataVeracity;
}
