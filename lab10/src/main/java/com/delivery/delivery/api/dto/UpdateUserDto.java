package com.delivery.delivery.api.dto;

public record UpdateUserDto (
        long id,

        String email,

        String login,

        String password
)
{}
