package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DefaultException extends RuntimeException{
    public DefaultException(String message) {
        super(message);
    }
}