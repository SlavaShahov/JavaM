package com.mrikh.homework_8.core.model.paymentType;

import com.mrikh.homework_8.core.model.Payment;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "check_payments")
@Getter
@Setter
public class Check extends Payment {
    private String name;
    @Column(name = "bank_id")
    private String bankID;
}