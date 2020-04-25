package com.hfad.parkingfinder.auth.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserPictureResponseDto {
    private PictureDataDto picture;
    private String id;
}

@Data
class PictureDataDto {
    private DataDto data;
}

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
class DataDto {
    private Integer height;
    private Integer width;
    private String url;
}