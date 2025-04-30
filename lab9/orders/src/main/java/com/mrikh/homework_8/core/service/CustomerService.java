package com.mrikh.homework_8.core.service;

import com.mrikh.homework_8.core.model.Customer;
import com.mrikh.homework_8.core.model.value.Address;
import com.mrikh.homework_8.core.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Customer createCustomer(String name, Address address) {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setAddress(address);
        return customerRepository.save(customer);
    }
}