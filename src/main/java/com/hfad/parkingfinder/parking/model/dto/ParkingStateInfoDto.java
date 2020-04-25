package com.hfad.parkingfinder.parking.model.dto;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkingStateInfoDto {
    private Integer parkingState;
    private Integer dataVeracity;
}
