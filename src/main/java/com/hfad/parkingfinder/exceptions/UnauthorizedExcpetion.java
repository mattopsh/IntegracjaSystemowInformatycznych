package com.hfad.parkingfinder.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UnauthorizedExcpetion extends RuntimeException {

    public UnauthorizedExcpetion() {
        super();
    }

    public UnauthorizedExcpetion(String message) {
        super(message);
    }
}