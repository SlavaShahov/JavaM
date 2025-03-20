package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(ExceptionHandler.class);

    @org.springframework.web.bind.annotation.ExceptionHandler(PetNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(PetNotFoundException e) {
        return new ErrorResponse("NOT_FOUND", e.getMessage(), 404);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({InvalidInputException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequest(Exception e) {
        return new ErrorResponse("BAD_REQUEST", e.getMessage(), 400);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse handleValidation(ValidationException e) {
        return new ErrorResponse("UNPROCESSABLE_ENTITY", e.getMessage(), 422);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGeneral(Exception e) {
        log.error("Unexpected error: ", e);
        return new ErrorResponse("INTERNAL_SERVER_ERROR", "Unexpected server error", 500);
    }

    public record ErrorResponse(String errorType, String message, int code) {}
}
