package com.hfad.parkingfinder.report.model.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtherInconsistencyDto {
    @NotEmpty
    private String description;
}

