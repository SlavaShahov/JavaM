package com.ordersapp;

import com.ordersapp.api.controller.OrderController;
import com.ordersapp.core.entity.Order;
import com.ordersapp.core.entity.PaymentType;
import com.ordersapp.core.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @Test
    void getOrdersByCustomerName_ShouldReturnOrders() throws Exception {
        // Arrange
        Order order = new Order();
        List<Order> orders = List.of(order);
        when(orderService.findOrdersByCustomerName("John")).thenReturn(orders);

        // Act & Assert
        mockMvc.perform(get("/orders/name")
                        .param("customerName", "John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getOrdersByPaymentStatus_ShouldReturnOrders() throws Exception {
        // Arrange
        Order order = new Order();
        List<Order> orders = List.of(order);
        when(orderService.findOrdersByPaymentStatus("PAID")).thenReturn(orders);

        // Act & Assert
        mockMvc.perform(get("/orders/payment-status")
                        .param("paymentStatus", "PAID"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getOrdersByOrderStatus_ShouldReturnOrders() throws Exception {
        // Arrange
        Order order = new Order();
        List<Order> orders = List.of(order);
        when(orderService.findOrdersByOrderStatus("PROCESSING")).thenReturn(orders);

        // Act & Assert
        mockMvc.perform(get("/orders/order-status")
                        .param("orderStatus", "PROCESSING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getOrdersByAddress_ShouldReturnOrders() throws Exception {
        // Arrange
        Order order = new Order();
        List<Order> orders = List.of(order);
        when(orderService.findOrdersByAddress("London", "Baker St", "NW1")).thenReturn(orders);

        // Act & Assert
        mockMvc.perform(get("/orders/address")
                        .param("city", "London")
                        .param("street", "Baker St")
                        .param("zipcode", "NW1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getOrdersByDateRange_ShouldReturnOrders() throws Exception {
        // Arrange
        Order order = new Order();
        List<Order> orders = List.of(order);
        LocalDateTime start = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2023, 1, 31, 23, 59);
        when(orderService.findOrdersByDateRange(start, end)).thenReturn(orders);

        // Act & Assert
        mockMvc.perform(get("/orders/date-range")
                        .param("startDate", "2023-01-01T00:00:00")
                        .param("endDate", "2023-01-31T23:59:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getOrdersByPaymentType_ShouldReturnOrders() throws Exception {
        // Arrange
        Order order = new Order();
        List<Order> orders = List.of(order);
        when(orderService.findOrdersByPaymentType(PaymentType.CREDIT)).thenReturn(orders);

        // Act & Assert
        mockMvc.perform(get("/orders/payment-type")
                        .param("paymentType", "CREDIT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}