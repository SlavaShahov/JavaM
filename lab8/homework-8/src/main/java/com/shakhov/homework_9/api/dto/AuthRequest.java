package com.shakhov.homework_9.api.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}