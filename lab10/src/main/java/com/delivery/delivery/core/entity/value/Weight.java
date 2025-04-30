package com.delivery.delivery.core.entity.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Weight {
    @Column(name = "shipping_weight")
    private BigDecimal value;

    @Column(name = "measurement_name", nullable = false)
    private String name;

    @Column(name = "measurement_symbol", nullable = false)
    private String symbol;


}