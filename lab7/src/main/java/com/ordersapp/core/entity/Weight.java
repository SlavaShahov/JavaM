package com.ordersapp.core.entity;

import com.ordersapp.core.entity.Measurement;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Embeddable
public class Weight extends Measurement {
    @Column(name = "shipping_weight")
    private BigDecimal value;

    public Weight(BigDecimal value) {
        super("Weight", "kg");
        this.value = value;
    }
}