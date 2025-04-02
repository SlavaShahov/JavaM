package com.ordersapp.api.controller;

import com.ordersapp.core.entity.Order;
import com.ordersapp.core.entity.PaymentType;
import com.ordersapp.core.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/name")
    public List<Order> getOrdersByCustomerName(@RequestParam(required = false) String customerName) {
        return orderService.findOrdersByCustomerName(customerName);
    }

    @GetMapping("/payment-status")
    public List<Order> getOrdersByPaymentStatus(@RequestParam(required = false) String paymentStatus) {
        return orderService.findOrdersByPaymentStatus(paymentStatus);
    }

    @GetMapping("/order-status")
    public List<Order> getOrdersByOrderStatus(@RequestParam(required = false) String orderStatus) {
        return orderService.findOrdersByOrderStatus(orderStatus);
    }

    @GetMapping("/address")
    public List<Order> getOrdersByAddress(@RequestParam(required = false) String city, @RequestParam(required = false) String street, @RequestParam(required = false) String zipcode) {
        return orderService.findOrdersByAddress(city, street, zipcode);
    }

    @GetMapping("/date-range")
    public List<Order> getOrdersByDateRange(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return orderService.findOrdersByDateRange(startDate, endDate);
    }

    @GetMapping("/payment-type")
    public List<Order> getOrdersByPaymentType(@RequestParam(required = false) PaymentType paymentType) {
        return orderService.findOrdersByPaymentType(paymentType);
    }

}
