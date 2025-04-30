package com.delivery.delivery.api.dto;

public record UserDto (
        String email,

        String login,

        String password,

        String name,
        String city,
        String street,
        String zipcode
)
{}
