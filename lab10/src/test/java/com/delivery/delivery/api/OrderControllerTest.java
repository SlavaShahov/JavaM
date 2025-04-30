package com.delivery.delivery.api;

import com.delivery.delivery.api.controller.OrderController;
import com.delivery.delivery.core.entity.value.Quantity;
import com.delivery.delivery.core.entity.value.Weight;
import com.delivery.delivery.core.repository.CustomerRepository;
import com.delivery.delivery.core.repository.ItemRepository;
import com.delivery.delivery.core.repository.OrderDetailRepository;
import com.delivery.delivery.core.repository.OrderRepository;
import com.delivery.delivery.core.repository.PaymentRepository;
import com.delivery.delivery.core.repository.TokenRepository;
import com.delivery.delivery.core.repository.UserRepository;
import org.springdoc.core.utils.PropertyResolverUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import com.delivery.delivery.core.entity.*;
import com.delivery.delivery.core.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private CustomerRepository customerRepository;

    @MockitoBean
    private PropertyResolverUtils propertyResolverUtils;

    @MockitoBean
    private OrderRepository orderRepository;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private TokenRepository tokenRepository;

    @MockitoBean
    private ItemRepository itemRepository;

    @MockitoBean
    private OrderDetailRepository orderDetailRepository;

    @MockitoBean
    private PaymentRepository paymentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Order order;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        authentication = new UsernamePasswordAuthenticationToken(
                "testuser",
                null,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Jane Doe");
        customer.setAddress(new Address("Los Angeles", "456 Oak St", "90001"));

        Item item = new Item();
        item.setId(1L);
        Weight weight = new Weight(BigDecimal.valueOf(2.0), "Weight", "kg");
        item.setMeasurement(weight);
        item.setDescription("Test Item");

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setId(1L);
        orderDetail.setQuantity(new Quantity(3, "шт", "Количество товара"));
        orderDetail.setTaxStatus("TAXABLE");
        orderDetail.setItem(item);

        Credit payment = new Credit();
        payment.setId(1L);
        payment.setAmount(150.0f);
        payment.setCreditNumber("1234-5678-9012-3456");
        payment.setCreditType("VISA");
        payment.setExpDate(LocalDateTime.parse("2027-03-28T10:00:00"));

        order = new Order();
        order.setId(1L);
        order.setDate(LocalDateTime.parse("2025-03-28T10:00:00"));
        order.setStatus("PENDING");
        order.setCustomer(customer);
        order.setPayment(payment);
        order.setOrderDetails(Collections.singletonList(orderDetail));

        orderDetail.setOrder(order);
        payment.setOrder(order);
    }

    @DisplayName("Получение заказов по адресу доставки")
    @Test
    void testGetOrdersByAddress() throws Exception {
        List<Order> expectedOrders = Collections.singletonList(order);
        when(orderService.findOrdersByAddress(authentication, "Los Angeles", null, "90001"))
                .thenReturn(expectedOrders);

        mockMvc.perform(get("/api/v1/orders/by-address")
                        .principal(authentication)
                        .param("city", "Los Angeles")
                        .param("zipcode", "90001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }

    @DisplayName("Получение заказов по временному интервалу")
    @Test
    void testGetOrdersByDateRange() throws Exception {
        LocalDateTime startDate = LocalDateTime.parse("2025-03-28T00:00:00");
        LocalDateTime endDate = LocalDateTime.parse("2028-03-29T23:59:59");
        List<Order> expectedOrders = Collections.singletonList(order);
        when(orderService.findOrdersByDateRange(authentication, startDate, endDate))
                .thenReturn(expectedOrders);

        mockMvc.perform(get("/api/v1/orders/by-date-range")
                        .principal(authentication)
                        .param("startDate", "2025-03-28T00:00:00")
                        .param("endDate", "2028-03-29T23:59:59"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].date").value("2025-03-28T10:00:00"))
                .andExpect(jsonPath("$[0].status").value("PENDING"))
                .andExpect(jsonPath("$[0].orderDetails[0].quantity.value").value(3));
    }

    @DisplayName("Получение заказов по способу оплаты")
    @Test
    void testGetOrdersByPaymentType() throws Exception {
        List<Order> expectedOrders = Collections.singletonList(order);
        when(orderService.findOrdersByPaymentType(authentication, PaymentType.CREDIT))
                .thenReturn(expectedOrders);

        mockMvc.perform(get("/api/v1/orders/by-payment-type")
                        .principal(authentication)
                        .param("paymentType", "CREDIT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].status").value("PENDING"))
                .andExpect(jsonPath("$[0].orderDetails[0].quantity.value").value(3));
    }

    @DisplayName("Получение заказов по способу оплаты - null")
    @Test
    void testGetOrdersByPaymentType_Null() throws Exception {
        List<Order> expectedOrders = Collections.singletonList(order);
        when(orderService.findOrdersByPaymentType(authentication, null))
                .thenReturn(expectedOrders);

        mockMvc.perform(get("/api/v1/orders/by-payment-type")
                        .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }

    @DisplayName("Получение заказов по имени пользователя")
    @Test
    void testGetOrdersByCustomerName() throws Exception {
        List<Order> expectedOrders = Collections.singletonList(order);
        when(orderService.findOrdersByCustomerName(authentication, "Jane Doe"))
                .thenReturn(expectedOrders);

        mockMvc.perform(get("/api/v1/orders/by-customer-name")
                        .principal(authentication)
                        .param("customerName", "Jane Doe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }

    @DisplayName("Получение заказов по статусу оплаты")
    @Test
    void testGetOrdersByPaymentStatus() throws Exception {
        List<Order> expectedOrders = Collections.singletonList(order);
        when(orderService.findOrdersByPaymentStatus(authentication, "PAID"))
                .thenReturn(expectedOrders);

        mockMvc.perform(get("/api/v1/orders/by-payment-status")
                        .principal(authentication)
                        .param("paymentStatus", "PAID"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }

    @DisplayName("Получение заказов по статусу оплаты - null")
    @Test
    void testGetOrdersByPaymentStatus_Null() throws Exception {
        List<Order> expectedOrders = Collections.singletonList(order);
        when(orderService.findOrdersByPaymentStatus(authentication, null))
                .thenReturn(expectedOrders);

        mockMvc.perform(get("/api/v1/orders/by-payment-status")
                        .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }

    @DisplayName("Получение заказов по статусу заказа")
    @Test
    void testGetOrdersByOrderStatus() throws Exception {
        List<Order> expectedOrders = Collections.singletonList(order);
        when(orderService.findOrdersByOrderStatus(authentication, "PENDING"))
                .thenReturn(expectedOrders);

        mockMvc.perform(get("/api/v1/orders/by-order-status")
                        .principal(authentication)
                        .param("orderStatus", "PENDING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }

    @DisplayName("Получение заказов по статусу заказа - null")
    @Test
    void testGetOrdersByOrderStatus_Null() throws Exception {
        List<Order> expectedOrders = Collections.singletonList(order);
        when(orderService.findOrdersByOrderStatus(authentication, null))
                .thenReturn(expectedOrders);

        mockMvc.perform(get("/api/v1/orders/by-order-status")
                        .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }

}
