package com.delivery.delivery.api;

import com.delivery.delivery.api.controller.UserController;
import com.delivery.delivery.api.dto.CustomerDto;
import com.delivery.delivery.api.dto.UpdateUserDto;
import com.delivery.delivery.api.dto.UserDto;
import com.delivery.delivery.core.entity.Address;
import com.delivery.delivery.core.entity.Customer;
import com.delivery.delivery.core.entity.User;
import com.delivery.delivery.core.repository.CustomerRepository;
import com.delivery.delivery.core.repository.ItemRepository;
import com.delivery.delivery.core.repository.OrderDetailRepository;
import com.delivery.delivery.core.repository.OrderRepository;
import com.delivery.delivery.core.repository.PaymentRepository;
import com.delivery.delivery.core.repository.TokenRepository;
import com.delivery.delivery.core.repository.UserRepository;
import com.delivery.delivery.core.service.OrderService;
import com.delivery.delivery.core.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springdoc.core.utils.PropertyResolverUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

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

    private User user;
    private Customer customer;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        authentication = new UsernamePasswordAuthenticationToken(
                "testuser",
                null,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        customer = new Customer();
        customer.setId(1L);
        customer.setName("Jane Doe");
        customer.setAddress(new Address("Los Angeles", "456 Oak St", "90001"));

        user = new User();
        user.setId(1L);
        user.setLogin("janedoe");
        user.setPassword("password123");
        user.setCustomer(customer);
    }

    @DisplayName("Создание пользователя")
    @Test
    void testCreateUser() throws Exception {
        UserDto userDto = new UserDto("janedoe@mail.ru", "testuser","password123", "Jane Doe", "Los Angeles", "456 Oak St", "90001");
        when(userService.createUser(userDto)).thenReturn(1L);

        mockMvc.perform(post("/api/v1/users")
                        .with(csrf())
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(1L));
    }

    @DisplayName("Обновление пользователя")
    @Test
    void testUpdateUser() throws Exception {
        UpdateUserDto preEditUser = new UpdateUserDto(1L, "janedoe_updated@mail.ru", "testuser",  "newpassword123");
        UpdateUserDto updatedUser = new UpdateUserDto(1L, "janedoe_updated@mail.ru", "testuser", "newpassword123");
        when(userService.updateUser(authentication, preEditUser)).thenReturn(updatedUser);

        mockMvc.perform(patch("/api/v1/users")
                        .with(csrf())
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(preEditUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @DisplayName("Обновление данных клиента")
    @Test
    void testUpdateCustomer() throws Exception {
        CustomerDto customerDto = new CustomerDto("Jane Doe Updated", "New York", "123 Main St", "10001");
        CustomerDto updatedCustomerDto = new CustomerDto("Jane Doe Updated", "New York", "123 Main St", "10001");
        when(userService.updateCustomer(authentication, customerDto)).thenReturn(updatedCustomerDto);

        mockMvc.perform(patch("/api/v1/users/customer")
                        .with(csrf())
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerDto)))
                .andExpect(status().isOk());
    }
}
