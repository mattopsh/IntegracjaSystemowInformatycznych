package com.hfad.parkingfinder.report.model.dto;

import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkingStateDto {
    @Range(min = -90, max = 90)
    private Double attitude;
    @Range(min = -180, max = 180)
    private Double longitude;
    private Integer parkingState;
    @Min(1)
    private Long parkingNodeId;
}
