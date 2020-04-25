package com.hfad.parkingfinder.parking.model.dto;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteParkingStateDto {
    private Long parkingNodeId;
    private Integer parkingState;
    private Integer dataVeracity;
}
