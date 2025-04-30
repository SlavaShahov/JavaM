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
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.core.Response;
import java.util.Collections;

@Service
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomerRepository customerRepository;
    private final Keycloak keycloak;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       CustomerRepository customerRepository, Keycloak keycloak) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.customerRepository = customerRepository;
        this.keycloak = keycloak;
    }

    @Transactional
    public Long createUser(UserDto body) {
        if (body.email() == null || body.login() == null || body.password() == null ||
                body.city() == null || body.street() == null || body.zipcode() == null) {
            throw new IllegalArgumentException("All fields (email, login, password, name, city, street, zipcode) must not be null");
        }

        // Создание пользователя в Keycloak
        UserRepresentation keycloakUser = new UserRepresentation();
        keycloakUser.setUsername(body.login());
        keycloakUser.setEmail(body.email());
        keycloakUser.setEnabled(true);
        keycloakUser.setEmailVerified(true);

        // Установка пароля
        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(body.password());
        keycloakUser.setCredentials(Collections.singletonList(passwordCred));

        // Создание пользователя в Keycloak
        Response response = keycloak.realm("my-realm").users().create(keycloakUser);
        if (response.getStatus() != 201) {
            throw new RuntimeException("Failed to create user in Keycloak: " + response.getStatusInfo());
        }

        // Получение Keycloak ID (sub)
        String keycloakId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

        // Назначение роли USER клиенту my-app
        String clientId = keycloak.realm("my-realm").clients().findByClientId("my-app").get(0).getId();
        RoleRepresentation userRole = keycloak.realm("my-realm").clients()
                .get(clientId).roles().get("USER").toRepresentation();
        keycloak.realm("my-realm").users().get(keycloakId).roles().clientLevel(clientId)
                .add(Collections.singletonList(userRole));

        // Создание пользователя в локальной базе
        User user = User.builder()
                .email(body.email())
                .login(body.login())
                .password(passwordEncoder.encode(body.password()))
                .role("USER")
                .sub(keycloakId) // Сохраняем Keycloak ID как sub
                .build();

        Customer customer = Customer.builder()
                .user(user)
                .name(body.login())
                .address(Address.builder()
                        .city(body.city())
                        .street(body.street())
                        .zipcode(body.zipcode())
                        .build())
                .build();

        user.setCustomer(customer);

        // Сохранение пользователя в базе
        user = userRepository.save(user);

        return user.getId();
    }

    public UpdateUserDto updateUser(Authentication authentication, UpdateUserDto body) {
        log.info("Updating user details for authentication: {}", authentication);
        Long clientId = getUserIdFromAuthentication(authentication);
        if (clientId == null) {
            throw new ForbiddenException("Access denied");
        }

        boolean hasAdminRole = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        if (!hasAdminRole) {
            throw new ForbiddenException("Access denied: User does not have ADMIN role");
        }

        User user = userRepository.findById(body.id())
                .orElseThrow(() -> new NotFoundException("User with ID: " + body.id() + " not found"));

        if (body.email() != null) {
            user.setEmail(body.email());
        }
        if (body.login() != null) {
            user.setLogin(body.login());
        }

        userRepository.save(user);
        log.info("User details updated: {}", user);

        return new UpdateUserDto(user.getId(), user.getEmail(), user.getLogin(), user.getPassword());
    }

    public User getUser(Authentication authentication) {
        log.info("Fetching user details for authentication: {}", authentication);
        Long id = getUserIdFromAuthentication(authentication);
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with ID: " + id + " not found"));
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return userRepository.findByEmail(login)
                .orElseThrow(() -> new NotFoundException("User with email: " + login + " not found"));
    }

    public User getUserById(Authentication authentication, long id) {
        Long clientId = getUserIdFromAuthentication(authentication);
        if (clientId == null) {
            throw new ForbiddenException("Access denied");
        }

        boolean hasAdminRole = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_USER"));
        if (!hasAdminRole) {
            throw new ForbiddenException("Access denied: User does not have USER role");
        }

        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id: " + id + " not found"));
    }

    public void deleteUser(Authentication authentication, long id) {
        Long clientId = getUserIdFromAuthentication(authentication);
        if (clientId == null) {
            throw new ForbiddenException("Access denied");
        }

        boolean hasAdminRole = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        if (!hasAdminRole) {
            throw new ForbiddenException("Access denied: User does not have ADMIN role");
        }

        userRepository.deleteUserById(id);
    }

    public CustomerDto updateCustomer(Authentication authentication, CustomerDto customerDto) {
        Long clientId = getUserIdFromAuthentication(authentication);
        if (clientId == null) {
            throw new ForbiddenException("Access denied");
        }
        Customer customer = customerRepository.findById(clientId)
                .orElseThrow(() -> new NotFoundException("Customer with ID: " + clientId + " not found"));

        if (customerDto.name() != null) {
            customer.setName(customerDto.name());
        }

        Address address = customer.getAddress();
        if (address == null) {
            address = new Address();
            customer.setAddress(address);
        }

        address.setCity(customerDto.city() != null ? customerDto.city() : address.getCity());
        address.setStreet(customerDto.street() != null ? customerDto.street() : address.getStreet());
        address.setZipcode(customerDto.zipcode() != null ? customerDto.zipcode() : address.getZipcode());

        if (address.getCity() == null || address.getStreet() == null || address.getZipcode() == null) {
            throw new IllegalArgumentException("City, street, and zipcode cannot be null");
        }

        customer.setAddress(address);

        customerRepository.save(customer);
        log.info("User details updated: {}", customer);

        return customerDto;
    }

    private Long getUserIdFromAuthentication(Authentication authentication) {
        if (authentication.getPrincipal() instanceof Jwt jwt) {
            String sub = jwt.getClaim("sub");
            if (sub != null) {
                User user = userRepository.findBySub(sub)
                        .orElseThrow(() -> new ForbiddenException("User not found for sub: " + sub));
                return user.getId();
            }
        }
        throw new ForbiddenException("Cannot extract user ID from authentication");
    }
}