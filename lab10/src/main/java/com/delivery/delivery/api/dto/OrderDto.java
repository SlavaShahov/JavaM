package com.delivery.delivery.api.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public record OrderDto(
        Long id,

        @NotNull(message = "Date cannot be null")
        LocalDateTime date,

        @NotNull(message = "Status cannot be null")
        String status,

        @NotNull(message = "Customer cannot be null")
        CustomerDto customer,

        PaymentDto payment,

        @NotNull(message = "Order details cannot be null")
        List<OrderDetailDto> orderDetails
) {
}
