package com.mrikh.homework_8.core.model.paymentType;

import com.mrikh.homework_8.core.model.Payment;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cash_payments")
@Getter
@Setter
public class Cash extends Payment {
    @Column(name = "cash_tendered")
    private float cashTendered;
}