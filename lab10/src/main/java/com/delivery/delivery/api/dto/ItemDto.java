package com.delivery.delivery.api.dto;

import jakarta.validation.constraints.NotNull;

public record ItemDto(
        Long id,

        @NotNull(message = "Shipping weight cannot be null")
        Double shippingWeight,

        @NotNull(message = "Description cannot be null")
        String description,

        @NotNull(message = "Measurement name cannot be null")
        String measurementName,

        @NotNull(message = "Measurement symbol cannot be null")
        String measurementSymbol
) {
}