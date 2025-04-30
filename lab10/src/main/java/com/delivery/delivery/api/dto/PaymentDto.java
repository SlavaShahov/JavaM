package com.delivery.delivery.api.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record PaymentDto(
        Long id,

        @NotNull(message = "Amount cannot be null")
        Float amount,

        @NotNull(message = "Payment type cannot be null")
        String paymentType,

        Float cashTendered, // Для Cash
        String name,        // Для Cash
        String bankId,      // Для Check
        String creditNumber,// Для Credit
        String creditType,  // Для Credit
        LocalDateTime expDate // Для Credit
) {
}