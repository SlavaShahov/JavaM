package com.delivery.delivery.api.dto;

import jakarta.validation.constraints.NotNull;

public record QuantityDto(
        @NotNull(message = "Quantity value cannot be null")
        Integer value,

        @NotNull(message = "Quantity unit cannot be null")
        String unit,

        @NotNull(message = "Quantity description cannot be null")
        String description
) {
}
