package com.ordersapp.core.service;

import com.ordersapp.core.entity.Order;
import com.ordersapp.core.entity.PaymentType;
import com.ordersapp.core.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public List<Order> findOrdersByAddress(String city, String street, String zipcode) {
        return orderRepository.findOrdersByAddress(city, street, zipcode);
    }

    public List<Order> findOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findOrdersByDateRange(startDate, endDate);
    }

    public List<Order> findOrdersByPaymentType(PaymentType paymentType) {
        String paymentTypeClass = paymentType != null ? getPaymentTypeClass(paymentType) : null;
        return orderRepository.findOrdersByPaymentType(paymentTypeClass);
    }

    public List<Order> findOrdersByCustomerName(String customerName) {
        return orderRepository.findOrdersByCustomerName(customerName);
    }

    public List<Order> findOrdersByPaymentStatus(String paymentStatus) {
        return orderRepository.findOrdersByPaymentStatus(paymentStatus);
    }

    public List<Order> findOrdersByOrderStatus(String orderStatus) {
        return orderRepository.findOrdersByStatus(orderStatus);
    }

    private String getPaymentTypeClass(PaymentType paymentType) {
        return switch (paymentType) {
            case CASH -> "Cash";
            case CHECK -> "Check";
            default -> "Credit";
        };
    }
}