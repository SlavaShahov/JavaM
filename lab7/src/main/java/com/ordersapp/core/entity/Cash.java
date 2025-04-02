package com.ordersapp.core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("CASH")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cash extends Payment {
    @Column(name = "cash_tendered")
    private Float cashTendered;
}
