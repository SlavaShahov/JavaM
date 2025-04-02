package com.ordersapp.core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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