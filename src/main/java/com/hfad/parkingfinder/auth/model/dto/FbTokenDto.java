package com.hfad.parkingfinder.auth.model.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FbTokenDto {
    @NotNull
    private String fbToken;
}
