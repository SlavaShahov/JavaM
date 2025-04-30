package com.mrikh.homework_8.core.model.value.measurements;

import com.mrikh.homework_8.core.model.value.Measurement;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Embeddable
@Getter
@Setter
public class Weight implements Measurement {
    @Column(name = "shipping_weight_value")
    private BigDecimal value;
    @Column(name = "shipping_weight_name")
    private String name;
    @Column(name = "shipping_weight_symbol")
    private String symbol;
}