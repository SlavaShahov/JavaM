package com.ordersapp.core.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@DiscriminatorValue("CHECK")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Check extends Payment {
    private String name;

    @Column(name = "bank_id")
    private String bankID;
}