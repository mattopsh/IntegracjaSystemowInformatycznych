package com.hfad.parkingfinder.auth.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDetailsResponseDto {
    private String first_name;
    private String last_name;
    private PictureDataDto picture;
}
