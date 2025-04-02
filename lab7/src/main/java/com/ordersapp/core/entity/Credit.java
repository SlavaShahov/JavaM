package com.ordersapp.core.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("CREDIT")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Credit extends Payment {
    @Column(name = "credit_number")
    private String creditNumber;

    @Column(name = "credit_type")
    private String creditType;

    @Column(name = "exp_date")
    private LocalDateTime expDate;
}
