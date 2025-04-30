package com.delivery.delivery.core.entity;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Measurement {
    @Column(name = "measurement_name", nullable = false)
    private String name;

    @Column(name = "measurement_symbol", nullable = false)
    private String symbol;
}