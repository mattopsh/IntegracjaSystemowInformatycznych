package com.hfad.parkingfinder.parking.model.dto;

import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;

@Getter
@Builder
public class ParkingStateDto {
    @Range(min = -90, max = 90)
    private Double attitude;
    @Range(min = -180, max = 180)
    private Double longitude;
    @Range(min = 0, max = 100)
    private Integer parkingState;
    @Min(1)
    private Long parkingNodeId;
}
