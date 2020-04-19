package com.hfad.parkingfinder.auth.model.dto;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponseDto {
    private String token;
}
