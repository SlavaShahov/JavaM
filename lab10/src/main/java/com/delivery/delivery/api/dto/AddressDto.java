package com.delivery.delivery.api.dto;

import jakarta.validation.constraints.NotNull;

public record AddressDto(
        @NotNull(message = "City cannot be null")
        String city,

        @NotNull(message = "Street cannot be null")
        String street,

        @NotNull(message = "Zipcode cannot be null")
        String zipcode
) {
}