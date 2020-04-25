package com.hfad.parkingfinder.auth.model.dto;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokensResponseDto {
    private String refreshToken;
    private String accessToken;
}
