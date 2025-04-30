package com.delivery.delivery.api.dto;

import jakarta.validation.constraints.NotNull;

public record OrderDetailDto(
        Long id,

        @NotNull(message = "Quantity cannot be null")
        QuantityDto quantity,

        @NotNull(message = "Tax status cannot be null")
        String taxStatus,

        @NotNull(message = "Item cannot be null")
        ItemDto item
) {
}