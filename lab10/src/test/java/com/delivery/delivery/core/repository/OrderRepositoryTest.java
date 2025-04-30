package com.delivery.delivery.core.repository;

import com.delivery.delivery.core.entity.Order;
import com.delivery.delivery.core.repository.OrderRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@Sql(scripts = "order_repository_test_resources.sql")
class OrderRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test-db")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private OrderRepository orderRepository;

    @BeforeAll
    static void setup() {
        postgres.start();
    }

    @AfterAll
    static void cleanup() {
        postgres.stop();
    }

    @Test
    @DisplayName("Поиск заказов по статусу заказа - SHIPPED")
    void testFindOrdersByStatus_Shipped() {
        List<Order> orders = orderRepository.findOrdersByStatus("SHIPPED");
        assertEquals(1, orders.size());
        assertEquals("SHIPPED", orders.get(0).getStatus());
        assertEquals("Alice Brown", orders.get(0).getCustomer().getName());
    }


    @Test
    @DisplayName("Поиск заказов по статусу заказа - несуществующий статус")
    void testFindOrdersByStatus_NonExistentStatus() {
        List<Order> orders = orderRepository.findOrdersByStatus("PENDING");
        assertTrue(orders.isEmpty());
    }

    @Test
    @DisplayName("Поиск заказов по адресу доставки - San Francisco")
    void testFindOrdersByAddress_SanFrancisco() {
        List<Order> orders = orderRepository.findOrdersByAddress("San Francisco", "123 Pine St", "94101");
        assertEquals(1, orders.size());
        assertEquals("San Francisco", orders.get(0).getCustomer().getAddress().getCity());
        assertEquals("123 Pine St", orders.get(0).getCustomer().getAddress().getStreet());
        assertEquals("94101", orders.get(0).getCustomer().getAddress().getZipcode());
    }

    @Test
    @DisplayName("Поиск заказов по адресу доставки - частичные параметры")
    void testFindOrdersByAddress_PartialParams() {
        List<Order> orders = orderRepository.findOrdersByAddress("San Francisco", null, null);
        assertEquals(1, orders.size());
        assertEquals("San Francisco", orders.get(0).getCustomer().getAddress().getCity());
    }

    @Test
    @DisplayName("Поиск заказов по адресу доставки - несуществующий адрес")
    void testFindOrdersByAddress_NonExistentAddress() {
        List<Order> orders = orderRepository.findOrdersByAddress("Los Angeles", null, null);
        assertTrue(orders.isEmpty());
    }

    @Test
    @DisplayName("Поиск заказов по временному интервалу - май 2025")
    void testFindOrdersByDateRange_May2025() {
        LocalDateTime startDate = LocalDateTime.parse("2025-05-01T00:00:00");
        LocalDateTime endDate = LocalDateTime.parse("2025-05-31T23:59:59");
        List<Order> orders = orderRepository.findOrdersByDateRange(startDate, endDate);
        assertEquals(1, orders.size());
        assertEquals(LocalDateTime.parse("2025-05-10T09:15:00"), orders.get(0).getDate());
        assertEquals("Alice Brown", orders.get(0).getCustomer().getName());
    }

    @Test
    @DisplayName("Поиск заказов по временному интервалу - вне диапазона")
    void testFindOrdersByDateRange_OutsideRange() {
        LocalDateTime startDate = LocalDateTime.parse("2025-06-01T00:00:00");
        LocalDateTime endDate = LocalDateTime.parse("2025-06-30T23:59:59");
        List<Order> orders = orderRepository.findOrdersByDateRange(startDate, endDate);
        assertTrue(orders.isEmpty());
    }

    @Test
    @DisplayName("Поиск заказов по имени пользователя - Alice Brown")
    void testFindOrdersByCustomerName_AliceBrown() {
        List<Order> orders = orderRepository.findOrdersByCustomerName("Alice Brown");
        assertEquals(1, orders.size());
        assertEquals("Alice Brown", orders.get(0).getCustomer().getName());
    }

    @Test
    @DisplayName("Поиск заказов по имени пользователя - несуществующее имя")
    void testFindOrdersByCustomerName_NonExistentName() {
        List<Order> orders = orderRepository.findOrdersByCustomerName("Jane Doe");
        assertTrue(orders.isEmpty());
    }
}
