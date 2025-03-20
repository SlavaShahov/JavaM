package com.example.demo.controller;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
