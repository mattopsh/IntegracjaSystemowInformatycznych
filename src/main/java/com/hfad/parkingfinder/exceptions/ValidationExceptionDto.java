package com.hfad.parkingfinder.exceptions;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ValidationExceptionDto {
    int status;
    String error;
    String message;
}
