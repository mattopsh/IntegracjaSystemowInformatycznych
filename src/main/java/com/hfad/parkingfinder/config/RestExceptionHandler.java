package com.hfad.parkingfinder.config;

import com.hfad.parkingfinder.exceptions.ValidationExceptionDto;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationExceptionDto handleMethodArgumentNotValid(MethodArgumentNotValidException methodArgumentNotValidException) {
        return ValidationExceptionDto.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(String.join(", ", methodArgumentNotValidException.getBindingResult().getFieldErrors()
                        .stream().map(error -> error.getField() + " " + error.getDefaultMessage()).collect(Collectors.toList())))
                .build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationExceptionDto handleValidationException(ConstraintViolationException constraintViolationException) {
        return ValidationExceptionDto.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(String.join(", ", constraintViolationException.getConstraintViolations()
                        .stream().map(error -> {
                            val fieldPath = error.getPropertyPath().toString();
                            val indexOfDot = fieldPath.indexOf(".");
                            if (indexOfDot == fieldPath.length() + 1) return fieldPath + " " + error.getMessage();
                            else return error.getPropertyPath().toString().substring(indexOfDot + 1) + " " + error.getMessage();
                        }).collect(Collectors.toList())))
                .build();
    }
}