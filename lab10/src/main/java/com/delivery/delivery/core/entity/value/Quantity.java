package com.delivery.delivery.core.entity.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Quantity {
    @Column(name = "quantity_value")
    private Integer value;

    @Column(name = "quantity_unit")
    private String unit;

    @Column(name = "quantity_description")
    private String description;
}