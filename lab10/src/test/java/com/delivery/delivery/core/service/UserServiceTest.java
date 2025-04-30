package com.delivery.delivery.core.service;

import com.delivery.delivery.api.dto.CustomerDto;
import com.delivery.delivery.api.dto.UpdateUserDto;
import com.delivery.delivery.api.dto.UserDto;
import com.delivery.delivery.core.entity.Address;
import com.delivery.delivery.core.entity.Customer;
import com.delivery.delivery.core.entity.User;
import com.delivery.delivery.core.exception.ForbiddenException;
import com.delivery.delivery.core.exception.NotFoundException;
import com.delivery.delivery.core.repository.CustomerRepository;
import com.delivery.delivery.core.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private CustomerRepository customerRepository;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    private Authentication authentication;
    private User user;
    private Customer customer;

    @BeforeEach
    void setUp() {
        authentication = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                "testuser",
                null,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );

        when(passwordEncoder.encode(anyString())).thenReturn("encoded_password");

        user = new User();
        user.setId(1L);
        user.setEmail("john.smith@example.com");
        user.setLogin("johnsmith");
        user.setPassword("encoded_password");
        user.setRole("USER");

        customer = new Customer();
        customer.setId(1L);
        customer.setName("John Smith");
        customer.setAddress(new Address("New York", "789 Maple Ave", "10001"));
        user.setCustomer(customer);
    }

//    @Test
//    @DisplayName("Создание пользователя - Успешный случай")
//    void testCreateUser_Success() {
//        UserDto userDto = new UserDto("john.smith@example.com", "johnsmith", "password123", "John Smith", "New York", "789 Maple Ave", "10001");
//        when(userRepository.save(any(User.class))).thenReturn(user);
//
//        Long userId = userService.createUser(userDto);
//
//        assertEquals(1L, userId);
//        verify(userRepository, times(1)).save(any(User.class));
//        verify(passwordEncoder, times(1)).encode("password123");
//    }

//    @Test
//    @DisplayName("Создание пользователя - ForbiddenException при отсутствии clientId")
//    void testCreateUser_ForbiddenException() {
//        when(jwtTokenUtils.getUserIdFromAuthentication(authentication)).thenReturn(null);
//        UserDto userDto = new UserDto("john.smith@example.com", "johnsmith", "password123", "John Smith", "New York", "789 Maple Ave", "10001");
//
//        assertThrows(ForbiddenException.class, () -> userService.createUser(authentication, userDto));
//        verify(userRepository, never()).save(any(User.class));
//    }

    @Test
    @DisplayName("Создание пользователя - IllegalArgumentException при null полях")
    void testCreateUser_IllegalArgumentException() {
        UserDto userDto = new UserDto(null, "johnsmith", "password123", "John Smith", "New York", "789 Maple Ave", "10001");

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(userDto));
        verify(userRepository, never()).save(any(User.class));
    }
//
//    @Test
//    @DisplayName("Обновление пользователя - Успешный случай")
//    void testUpdateUser_Success() {
//        UpdateUserDto updateUserDto = new UpdateUserDto(1L, "john.smith.updated@example.com", "johnsmith_updated", null);
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//        when(userRepository.save(any(User.class))).thenReturn(user);
//
//        UpdateUserDto result = userService.updateUser(authentication, updateUserDto);
//
//        assertEquals(1L, result.id());
//        assertEquals("john.smith.updated@example.com", result.email());
//        assertEquals("johnsmith_updated", result.login());
//        assertEquals("encoded_password", result.password());
//        verify(userRepository, times(1)).save(user);
//    }
//
//
//    @Test
//    @DisplayName("Обновление пользователя - NotFoundException при отсутствии пользователя")
//    void testUpdateUser_NotFoundException() {
//        UpdateUserDto updateUserDto = new UpdateUserDto(1L, "john.smith.updated@example.com", "johnsmith_updated", null);
//        when(userRepository.findById(1L)).thenReturn(Optional.empty());
//
//        assertThrows(NotFoundException.class, () -> userService.updateUser(authentication, updateUserDto));
//        verify(userRepository, never()).save(any(User.class));
//    }

    @Test
    @DisplayName("Загрузка пользователя по email - Успешный случай")
    void testLoadUserByUsername_Success() {
        when(userRepository.findByEmail("john.smith@example.com")).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername("john.smith@example.com");

        assertEquals(user, userDetails);
        verify(userRepository, times(1)).findByEmail("john.smith@example.com");
    }

    @Test
    @DisplayName("Загрузка пользователя по email - NotFoundException при отсутствии пользователя")
    void testLoadUserByUsername_NotFoundException() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.loadUserByUsername("nonexistent@example.com"));
        verify(userRepository, times(1)).findByEmail("nonexistent@example.com");
    }

//    @Test
//    @DisplayName("Получение пользователя по логину - Успешный случай")
//    void testGetUserByLogin_Success() {
//        when(userRepository.findByLogin("johnsmith")).thenReturn(Optional.of(user));
//
//        User result = userService.getUserByLogin("johnsmith");
//
//        assertEquals(user, result);
//        verify(userRepository, times(1)).findByLogin("johnsmith");
//    }
//
//    @Test
//    @DisplayName("Получение пользователя по логину - NotFoundException при отсутствии пользователя")
//    void testGetUserByLogin_NotFoundException() {
//        when(userRepository.findByLogin("nonexistent")).thenReturn(Optional.empty());
//
//        assertThrows(NotFoundException.class, () -> userService.getUserByLogin("nonexistent"));
//        verify(userRepository, times(1)).findByLogin("nonexistent");
//    }

//    @Test
//    @DisplayName("Получение пользователя по ID - Успешный случай")
//    void testGetUserById_Success() {
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//
//        User result = userService.getUserById(authentication, 1L);
//
//        assertEquals(user, result);
//        verify(userRepository, times(1)).findById(1L);
//    }
////
//    @Test
//    @DisplayName("Получение пользователя по ID - ForbiddenException при отсутствии clientId")
//    void testGetUserById_ForbiddenException() {
//        when(jwtTokenUtils.getUserIdFromAuthentication(authentication)).thenReturn(null);
//
//        assertThrows(ForbiddenException.class, () -> userService.getUserById(authentication, 1L));
//        verify(userRepository, never()).findById(anyLong());
//    }

//    @Test
//    @DisplayName("Получение пользователя по ID - NotFoundException при отсутствии пользователя")
//    void testGetUserById_NotFoundException() {
//        when(userRepository.findById(1L)).thenReturn(Optional.empty());
//
//        assertThrows(NotFoundException.class, () -> userService.getUserById(authentication, 1L));
//        verify(userRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    @DisplayName("Удаление пользователя - Успешный случай")
//    void testDeleteUser_Success() {
//        doNothing().when(userRepository).deleteUserById(1L);
//
//        userService.deleteUser(authentication, 1L);
//
//        verify(userRepository, times(1)).deleteUserById(1L);
//    }

//    @Test
//    @DisplayName("Удаление пользователя - ForbiddenException при отсутствии clientId")
//    void testDeleteUser_ForbiddenException() {
//        when(jwtTokenUtils.getUserIdFromAuthentication(authentication)).thenReturn(null);
//
//        assertThrows(ForbiddenException.class, () -> userService.deleteUser(authentication, 1L));
//        verify(userRepository, never()).deleteUserById(anyLong());
//    }

//    @Test
//    @DisplayName("Обновление данных клиента - Успешный случай")
//    void testUpdateCustomer_Success() {
//        CustomerDto customerDto = new CustomerDto("John Smith Updated", "San Francisco", "123 Pine St", "94101");
//        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
//        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
//
//        CustomerDto result = userService.updateCustomer(authentication, customerDto);
//
//        assertEquals("John Smith Updated", result.name());
//        assertEquals("San Francisco", result.city());
//        assertEquals("123 Pine St", result.street());
//        assertEquals("94101", result.zipcode());
//        verify(customerRepository, times(1)).save(customer);
//    }
//
//    @Test
//    @DisplayName("Обновление данных клиента - ForbiddenException при отсутствии clientId")
//    void testUpdateCustomer_ForbiddenException() {
//        when(jwtTokenUtils.getUserIdFromAuthentication(authentication)).thenReturn(null);
//        CustomerDto customerDto = new CustomerDto("John Smith Updated", "San Francisco", "123 Pine St", "94101");
//
//        assertThrows(ForbiddenException.class, () -> userService.updateCustomer(authentication, customerDto));
//        verify(customerRepository, never()).save(any(Customer.class));
//    }
//
//    @Test
//    @DisplayName("Обновление данных клиента - NotFoundException при отсутствии клиента")
//    void testUpdateCustomer_NotFoundException() {
//        CustomerDto customerDto = new CustomerDto("John Smith Updated", "San Francisco", "123 Pine St", "94101");
//        when(customerRepository.findById(1L)).thenReturn(Optional.empty());
//
//        assertThrows(NotFoundException.class, () -> userService.updateCustomer(authentication, customerDto));
//        verify(customerRepository, never()).save(any(Customer.class));
//    }
//
//    @Test
//    @DisplayName("Обновление данных клиента - IllegalArgumentException при null полях адреса")
//    void testUpdateCustomer_IllegalArgumentException() {
//        CustomerDto customerDto = new CustomerDto("John Smith Updated", null, "123 Pine St", "94101");
//        customer.setAddress(null);
//        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
//
//        assertThrows(IllegalArgumentException.class, () -> userService.updateCustomer(authentication, customerDto));
//        verify(customerRepository, never()).save(any(Customer.class));
//    }
}
