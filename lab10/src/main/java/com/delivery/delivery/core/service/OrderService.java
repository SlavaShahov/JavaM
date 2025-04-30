package com.delivery.delivery.core.service;

import com.delivery.delivery.api.dto.OrderDetailDto;
import com.delivery.delivery.api.dto.OrderDto;
import com.delivery.delivery.api.dto.PaymentDto;
import com.delivery.delivery.core.entity.Cash;
import com.delivery.delivery.core.entity.Check;
import com.delivery.delivery.core.entity.Credit;
import com.delivery.delivery.core.entity.Customer;
import com.delivery.delivery.core.entity.Item;
import com.delivery.delivery.core.entity.Order;
import com.delivery.delivery.core.entity.OrderDetail;
import com.delivery.delivery.core.entity.Payment;
import com.delivery.delivery.core.entity.PaymentType;
import com.delivery.delivery.core.entity.User;
import com.delivery.delivery.core.entity.value.Quantity;
import com.delivery.delivery.core.entity.value.Weight;
import com.delivery.delivery.core.exception.ForbiddenException;
import com.delivery.delivery.core.exception.NotFoundException;
import com.delivery.delivery.core.repository.CustomerRepository;
import com.delivery.delivery.core.repository.ItemRepository;
import com.delivery.delivery.core.repository.OrderDetailRepository;
import com.delivery.delivery.core.repository.OrderRepository;
import com.delivery.delivery.core.repository.PaymentRepository;
import com.delivery.delivery.core.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    //private final JwtTokenUtils jwtTokenUtils;
    private final CustomerRepository customerRepository;
    private final ItemRepository itemRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;

    @Transactional
    public OrderDto createOrder(Authentication authentication, OrderDto orderDto) {
        Long clientId = getUserIdFromAuthentication(authentication);
        if (clientId == null) {
            throw new ForbiddenException("Access denied");
        }

        Customer customer = customerRepository.findById(clientId)
                .orElseThrow(() -> new NotFoundException("Customer with ID: " + clientId + " not found"));

        Order order = new Order();
        order.setDate(orderDto.date());
        order.setStatus(orderDto.status());
        order.setCustomer(customer);

        order = orderRepository.save(order);

        for (OrderDetailDto detailDto : orderDto.orderDetails()) {
            Item item = new Item();
            Weight weight = new Weight(
                    BigDecimal.valueOf(detailDto.item().shippingWeight()),
                    detailDto.item().measurementName(),
                    detailDto.item().measurementSymbol()
            );
            item.setMeasurement(weight);
            item.setDescription(detailDto.item().description());
            item = itemRepository.save(item);

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setQuantity(new Quantity(
                    detailDto.quantity().value(),
                    detailDto.quantity().unit(),
                    detailDto.quantity().description()
            ));
            orderDetail.setTaxStatus(detailDto.taxStatus());
            orderDetail.setItem(item);
            orderDetail.setOrder(order);

            order.getOrderDetails().add(orderDetail);
            orderDetailRepository.save(orderDetail);
        }

        Payment payment = null;
        PaymentDto paymentDto = orderDto.payment();
        if (paymentDto != null) {
            switch (paymentDto.paymentType()) {
                case "CASH":
                    Cash cash = new Cash();
                    cash.setAmount(paymentDto.amount());
                    cash.setCashTendered(paymentDto.cashTendered());
                    payment = cash;
                    break;
                case "CHECK":
                    Check check = new Check();
                    check.setAmount(paymentDto.amount());
                    check.setName(paymentDto.name());
                    payment = check;
                    break;
                case "CREDIT":
                    Credit credit = new Credit();
                    credit.setAmount(paymentDto.amount());
                    credit.setCreditNumber(paymentDto.creditNumber());
                    credit.setCreditType(paymentDto.creditType());
                    credit.setExpDate(paymentDto.expDate());
                    payment = credit;
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported payment type: " + paymentDto.paymentType());
            }
            payment.setOrder(order);
            order.setPayment(payment);
            paymentRepository.save(payment);
        }

        order = orderRepository.save(order);

        log.info("Order created: id={}, date={}, status={}, customerId={}",
                order.getId(), order.getDate(), order.getStatus(), customer.getId());

        return orderDto;
    }

    public List<Order> findOrdersByAddress(Authentication authentication, String city, String street, String zipcode) {
        Long clientId = getUserIdFromAuthentication(authentication);
        if (clientId == null) {
            throw new ForbiddenException("Access denied");
        }
        return orderRepository.findOrdersByAddress(city, street, zipcode);
    }

    public List<Order> findOrdersByDateRange(Authentication authentication, LocalDateTime startDate, LocalDateTime endDate) {
        Long clientId = getUserIdFromAuthentication(authentication);
        if (clientId == null) {
            throw new ForbiddenException("Access denied");
        }
        return orderRepository.findOrdersByDateRange(startDate, endDate);
    }

    public List<Order> findOrdersByPaymentType(Authentication authentication, PaymentType paymentType) {
        Long clientId = getUserIdFromAuthentication(authentication);
        if (clientId == null) {
            throw new ForbiddenException("Access denied");
        }
        String paymentTypeClass = paymentType != null ? getPaymentTypeClass(paymentType) : null;
        return orderRepository.findOrdersByPaymentType(paymentTypeClass);
    }

    public List<Order> findOrdersByCustomerName(Authentication authentication, String customerName) {
        Long clientId = getUserIdFromAuthentication(authentication);
        if (clientId == null) {
            throw new ForbiddenException("Access denied");
        }
        return orderRepository.findOrdersByCustomerName(customerName);
    }

    public List<Order> findOrdersByPaymentStatus(Authentication authentication, String paymentStatus) {
        Long clientId = getUserIdFromAuthentication(authentication);
        if (clientId == null) {
            throw new ForbiddenException("Access denied");
        }
        return orderRepository.findOrdersByPaymentStatus(paymentStatus);
    }

    public List<Order> findOrdersByOrderStatus(Authentication authentication, String orderStatus) {
        Long clientId = getUserIdFromAuthentication(authentication);
        if (clientId == null) {
            throw new ForbiddenException("Access denied");
        }
        return orderRepository.findOrdersByStatus(orderStatus);
    }

    private String getPaymentTypeClass(PaymentType paymentType) {
        return switch (paymentType) {
            case CASH -> "Cash";
            case CHECK -> "Check";
            default -> "Credit";
        };
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