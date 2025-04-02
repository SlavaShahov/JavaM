package com.ordersapp;

import com.ordersapp.core.entity.Order;
import com.ordersapp.core.entity.PaymentType;
import com.ordersapp.core.repository.OrderRepository;
import com.ordersapp.core.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void findOrdersByCustomerName_ShouldReturnOrders() {
        // Arrange
        Order order1 = new Order();
        Order order2 = new Order();
        List<Order> expectedOrders = Arrays.asList(order1, order2);
        when(orderRepository.findOrdersByCustomerName("John Doe")).thenReturn(expectedOrders);

        // Act
        List<Order> result = orderService.findOrdersByCustomerName("John Doe");

        // Assert
        assertEquals(2, result.size());
        verify(orderRepository, times(1)).findOrdersByCustomerName("John Doe");
    }

    @Test
    void findOrdersByPaymentStatus_ShouldReturnPaidOrders() {
        // Arrange
        Order paidOrder = new Order();
        List<Order> expectedOrders = List.of(paidOrder);
        when(orderRepository.findOrdersByPaymentStatus("PAID")).thenReturn(expectedOrders);

        // Act
        List<Order> result = orderService.findOrdersByPaymentStatus("PAID");

        // Assert
        assertEquals(1, result.size());
        verify(orderRepository, times(1)).findOrdersByPaymentStatus("PAID");
    }

    @Test
    void findOrdersByOrderStatus_ShouldReturnOrders() {
        // Arrange
        Order order = new Order();
        List<Order> expectedOrders = List.of(order);
        when(orderRepository.findOrdersByStatus("SHIPPED")).thenReturn(expectedOrders);

        // Act
        List<Order> result = orderService.findOrdersByOrderStatus("SHIPPED");

        // Assert
        assertEquals(1, result.size());
        verify(orderRepository, times(1)).findOrdersByStatus("SHIPPED");
    }

    @Test
    void findOrdersByAddress_ShouldReturnOrders() {
        // Arrange
        Order order = new Order();
        List<Order> expectedOrders = List.of(order);
        when(orderRepository.findOrdersByAddress("New York", "Broadway", "10001"))
                .thenReturn(expectedOrders);

        // Act
        List<Order> result = orderService.findOrdersByAddress("New York", "Broadway", "10001");

        // Assert
        assertEquals(1, result.size());
        verify(orderRepository, times(1))
                .findOrdersByAddress("New York", "Broadway", "10001");
    }

    @Test
    void findOrdersByDateRange_ShouldReturnOrders() {
        // Arrange
        LocalDateTime start = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2023, 1, 31, 23, 59);
        Order order = new Order();
        List<Order> expectedOrders = List.of(order);
        when(orderRepository.findOrdersByDateRange(start, end)).thenReturn(expectedOrders);

        // Act
        List<Order> result = orderService.findOrdersByDateRange(start, end);

        // Assert
        assertEquals(1, result.size());
        verify(orderRepository, times(1)).findOrdersByDateRange(start, end);
    }

    @Test
    void findOrdersByPaymentType_ShouldReturnCreditOrders() {
        // Arrange
        Order order = new Order();
        List<Order> expectedOrders = List.of(order);
        when(orderRepository.findOrdersByPaymentType("Credit")).thenReturn(expectedOrders);

        // Act
        List<Order> result = orderService.findOrdersByPaymentType(PaymentType.CREDIT);

        // Assert
        assertEquals(1, result.size());
        verify(orderRepository, times(1)).findOrdersByPaymentType("Credit");
    }

    @Test
    void findOrdersByPaymentType_NullType_ShouldReturnAllOrders() {
        // Arrange
        Order order1 = new Order();
        Order order2 = new Order();
        List<Order> expectedOrders = Arrays.asList(order1, order2);
        when(orderRepository.findOrdersByPaymentType(null)).thenReturn(expectedOrders);

        // Act
        List<Order> result = orderService.findOrdersByPaymentType(null);

        // Assert
        assertEquals(2, result.size());
        verify(orderRepository, times(1)).findOrdersByPaymentType(null);
    }
}