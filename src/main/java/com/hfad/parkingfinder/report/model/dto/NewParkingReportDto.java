package com.hfad.parkingfinder.report.model.dto;

import com.hfad.parkingfinder.parking.model.Cost;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewParkingReportDto {
    @NotNull
    @Range(min = -90, max = 90)
    private Double attitude;
    @NotNull
    @Range(min = -180, max = 180)
    private Double longitude;
    @Range(min = 5, max = 1000)
    private Integer capacity;
    private Cost stayCost;
    private String otherInformation;
}
