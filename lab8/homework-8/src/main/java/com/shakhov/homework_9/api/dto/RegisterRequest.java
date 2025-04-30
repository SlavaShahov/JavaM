package com.shakhov.homework_9.api.dto;

import com.shakhov.homework_9.core.model.value.Address;
import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private Address address;
    private String username;
    private String password;
}