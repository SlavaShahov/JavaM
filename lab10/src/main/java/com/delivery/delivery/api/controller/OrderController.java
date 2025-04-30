package com.delivery.delivery.api.controller;

import com.delivery.delivery.api.dto.OrderDto;
import com.delivery.delivery.core.entity.Order;
import com.delivery.delivery.core.entity.PaymentType;
import com.delivery.delivery.core.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/by-address")
    @PreAuthorize("hasRole('USER')")
    public List<Order> getOrdersByAddress(
            Authentication authentication,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String street,
            @RequestParam(required = false) String zipcode
    ) {
        return orderService.findOrdersByAddress(authentication, city, street, zipcode);
    }

    @GetMapping("/by-date-range")
    @PreAuthorize("hasRole('USER')")
    public List<Order> getOrdersByDateRange(
            Authentication authentication,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return orderService.findOrdersByDateRange(authentication, startDate, endDate);
    }

    @GetMapping("/by-payment-type")
    @PreAuthorize("hasRole('USER')")
    public List<Order> getOrdersByPaymentType(Authentication authentication, @RequestParam(required = false) PaymentType paymentType) {
        return orderService.findOrdersByPaymentType(authentication, paymentType);
    }

    @GetMapping("/by-customer-name")
    @PreAuthorize("hasRole('USER')")
    public List<Order> getOrdersByCustomerName(Authentication authentication, @RequestParam(required = false) String customerName) {
        return orderService.findOrdersByCustomerName(authentication, customerName);
    }

    @GetMapping("/by-payment-status")
    @PreAuthorize("hasRole('USER')")
    public List<Order> getOrdersByPaymentStatus(Authentication authentication, @RequestParam(required = false) String paymentStatus) {
        return orderService.findOrdersByPaymentStatus(authentication, paymentStatus);
    }

    @GetMapping("/by-order-status")
    @PreAuthorize("hasRole('USER')")
    public List<Order> getOrdersByOrderStatus(Authentication authentication, @RequestParam(required = false) String orderStatus) {
        return orderService.findOrdersByOrderStatus(authentication, orderStatus);
    }

    @PostMapping("/new-order")
    @PreAuthorize("hasRole('USER')")
    public OrderDto createOrder(Authentication authentication, @RequestBody OrderDto orderDto) {
        return orderService.createOrder(authentication, orderDto);
    }


}
