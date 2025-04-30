package com.shakhov.homework_9.core.service;

import com.shakhov.homework_9.api.dto.OrderRequest;
import com.shakhov.homework_9.core.model.Customer;
import com.shakhov.homework_9.core.model.Item;
import com.shakhov.homework_9.core.model.OrderDetail;
import com.shakhov.homework_9.core.model.PaymentStatus;
import com.shakhov.homework_9.core.model.paymentType.Cash;
import com.shakhov.homework_9.core.model.paymentType.Check;
import com.shakhov.homework_9.core.model.paymentType.Credit;
import com.shakhov.homework_9.core.model.value.Address;
import com.shakhov.homework_9.core.model.value.measurements.Quantity;
import com.shakhov.homework_9.core.model.Order;
import com.shakhov.homework_9.core.repository.CustomerRepository;
import com.shakhov.homework_9.core.repository.ItemRepository;
import com.shakhov.homework_9.core.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
public class OrderServiceIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ItemRepository itemRepository;

    private Long customerId1;
    private Long customerId2;
    private Long itemId1;
    private Long itemId2;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        customerRepository.deleteAll();
        itemRepository.deleteAll();

        Item item1 = new Item();
        item1.setDescription("Test Item 1");
        itemRepository.save(item1);
        itemId1 = item1.getId();

        Item item2 = new Item();
        item2.setDescription("Test Item 2");
        itemRepository.save(item2);
        itemId2 = item2.getId();

        Customer customer1 = new Customer();
        customer1.setName("John Doe");
        Address address1 = new Address();
        address1.setCity("New York");
        address1.setStreet("123 Broadway");
        address1.setZipcode("10001");
        customer1.setAddress(address1);
        customerRepository.save(customer1);
        customerId1 = customer1.getId();

        Customer customer2 = new Customer();
        customer2.setName("Jane Smith");
        Address address2 = new Address();
        address2.setCity("Los Angeles");
        address2.setStreet("456 Hollywood Blvd");
        address2.setZipcode("90001");
        customer2.setAddress(address2);
        customerRepository.save(customer2);
        customerId2 = customer2.getId();

        Order order1 = new Order();
        order1.setCustomer(customer1);
        order1.setDate(LocalDateTime.now().minusDays(5));
        order1.setStatus("SHIPPED");

        OrderDetail detail1 = new OrderDetail();
        detail1.setItem(item1);
        detail1.setQuantity(new Quantity(2, "piece", "pc"));
        detail1.setTaxStatus("TAXABLE");
        order1.addOrderDetail(detail1);

        Cash cashPayment = new Cash();
        cashPayment.setAmount(100.0f);
        cashPayment.setCashTendered(100.0f);
        cashPayment.setStatus(PaymentStatus.COMPLETED);
        cashPayment.setOrder(order1);
        order1.setPayment(cashPayment);

        orderRepository.save(order1);

        Order order2 = new Order();
        order2.setCustomer(customer2);
        order2.setDate(LocalDateTime.now().minusDays(2));
        order2.setStatus("PROCESSING");

        OrderDetail detail2 = new OrderDetail();
        detail2.setItem(item2);
        detail2.setQuantity(new Quantity(1, "piece", "pc"));
        detail2.setTaxStatus("TAX_EXEMPT");
        order2.addOrderDetail(detail2);

        Credit creditPayment = new Credit();
        creditPayment.setAmount(200.0f);
        creditPayment.setNumber("1234-5678-9012-3456");
        creditPayment.setCardType("VISA");
        creditPayment.setExpDate(LocalDateTime.now().plusYears(2));
        creditPayment.setStatus(PaymentStatus.PENDING);
        creditPayment.setOrder(order2);
        order2.setPayment(creditPayment);

        orderRepository.save(order2);

        Order order3 = new Order();
        order3.setCustomer(customer1);
        order3.setDate(LocalDateTime.now().minusDays(1));
        order3.setStatus("CANCELLED");

        OrderDetail detail3 = new OrderDetail();
        detail3.setItem(item1);
        detail3.setQuantity(new Quantity(3, "piece", "pc"));
        detail3.setTaxStatus("TAXABLE");
        order3.addOrderDetail(detail3);

        Check checkPayment = new Check();
        checkPayment.setAmount(150.0f);
        checkPayment.setName("John Doe");
        checkPayment.setBankID("BANK123");
        checkPayment.setStatus(PaymentStatus.FAILED);
        checkPayment.setOrder(order3);
        order3.setPayment(checkPayment);

        orderRepository.save(order3);
    }

    @Test
    void shouldFindOrdersByCustomerNameContainingSubstring() {
        List<Order> orders = orderService.searchOrders(
                "John", null, null, null,
                null, null, null, null, null);

        assertThat(orders).hasSize(2);
        assertThat(orders).extracting(Order::getCustomer).extracting(Customer::getName)
                .containsExactly("John Doe", "John Doe");
    }

    @Test
    void shouldFindOrdersByCustomerAddressCity() {
        List<Order> orders = orderService.searchOrders(
                null, "Los Angeles", null, null,
                null, null, null, null, null);

        assertThat(orders).hasSize(1);
        assertThat(orders.get(0).getCustomer().getAddress().getCity()).isEqualTo("Los Angeles");
    }

    @Test
    void shouldFindOrdersWithinDateRange() {
        LocalDateTime fromDate = LocalDateTime.now().minusDays(4);
        LocalDateTime toDate = LocalDateTime.now().minusDays(1);

        List<Order> orders = orderService.searchOrders(
                null, null, null, null,
                fromDate, toDate, null, null, null);

        assertThat(orders).hasSize(2);
    }

    @Test
    void shouldFindOrdersByPaymentStatusAndStatus() {
        List<Order> orders = orderService.searchOrders(
                null, null, null, null,
                null, null, null, "PROCESSING", PaymentStatus.PENDING.name());

        assertThat(orders).hasSize(1);
        assertThat(orders.get(0).getStatus()).isEqualTo("PROCESSING");
        assertThat(orders.get(0).getPayment().getStatus()).isEqualTo(PaymentStatus.PENDING);
    }

    @Test
    void shouldFindOrdersWithMultipleCriteria() {
        List<Order> orders = orderService.searchOrders(
                "John", "New York", null, null,
                null, null, "check", null, null);

        assertThat(orders).hasSize(1);
        assertThat(orders.getFirst().getCustomer().getName()).isEqualTo("John Doe");
        assertThat(orders.getFirst().getPayment()).isInstanceOf(Check.class);
    }

    @Test
    void shouldCreateOrderWithMultipleDetails() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setCustomerId(customerId1);
        orderRequest.setStatus("NEW");
        orderRequest.setDate(LocalDateTime.now());

        List<OrderRequest.OrderDetailDto> details = new ArrayList<>();
        OrderRequest.OrderDetailDto detailDto1 = new OrderRequest.OrderDetailDto();
        detailDto1.setItemId(itemId1);
        detailDto1.setTaxStatus("TAXABLE");

        OrderRequest.QuantityDto quantityDto1 = new OrderRequest.QuantityDto();
        quantityDto1.setAmount(3);
        quantityDto1.setUnit("piece");
        quantityDto1.setUnitAbbreviation("pc");
        detailDto1.setQuantity(quantityDto1);

        details.add(detailDto1);

        orderRequest.setOrderDetails(details);

        OrderRequest.PaymentDto paymentDto = new OrderRequest.PaymentDto();
        paymentDto.setPaymentType("CREDIT");
        paymentDto.setAmount(500.0f);
        paymentDto.setStatus("PENDING");
        paymentDto.setCardNumber("4111-1111-1111-1111");
        paymentDto.setCardType("MASTERCARD");
        paymentDto.setExpiryDate(LocalDateTime.now().plusYears(1));
        orderRequest.setPayment(paymentDto);

        Order createdOrder = orderService.createOrder(orderRequest);

        assertNotNull(createdOrder.getId());
        assertEquals("NEW", createdOrder.getStatus());
        assertEquals(customerId1, createdOrder.getCustomer().getId());
        assertEquals(1, createdOrder.getOrderDetails().size());
        assertEquals(itemId1, createdOrder.getOrderDetails().getFirst().getItem().getId());
    }

    @Test
    void shouldUpdateOrderAndReflectChanges() {
        OrderRequest createRequest = new OrderRequest();
        createRequest.setCustomerId(customerId1);
        createRequest.setStatus("NEW");

        OrderRequest.OrderDetailDto detailDto = new OrderRequest.OrderDetailDto();
        detailDto.setItemId(itemId1);
        detailDto.setTaxStatus("TAXABLE");

        OrderRequest.QuantityDto quantityDto = new OrderRequest.QuantityDto();
        quantityDto.setAmount(4);
        quantityDto.setUnit("piece");
        quantityDto.setUnitAbbreviation("pc");
        detailDto.setQuantity(quantityDto);

        createRequest.setOrderDetails(Collections.singletonList(detailDto));

        OrderRequest.PaymentDto paymentDto = new OrderRequest.PaymentDto();
        paymentDto.setPaymentType("CASH");
        paymentDto.setAmount(100.0f);
        createRequest.setPayment(paymentDto);

        Order createdOrder = orderService.createOrder(createRequest);

        Long orderId = createdOrder.getId();

        OrderRequest updateRequest = new OrderRequest();
        updateRequest.setStatus("SHIPPED");
        updateRequest.setCustomerId(customerId2);

        OrderRequest.OrderDetailDto updatedDetail = new OrderRequest.OrderDetailDto();
        updatedDetail.setItemId(itemId2);
        updatedDetail.setTaxStatus("TAX_EXEMPT");

        OrderRequest.QuantityDto updatedQuantity = new OrderRequest.QuantityDto();
        updatedQuantity.setAmount(5);
        updatedQuantity.setUnit("kilogram");
        updatedDetail.setQuantity(updatedQuantity);

        updateRequest.setOrderDetails(Collections.singletonList(updatedDetail));

        Order updatedOrder = orderService.updateOrder(orderId, updateRequest);

        assertEquals("SHIPPED", updatedOrder.getStatus());
        assertEquals(customerId2, updatedOrder.getCustomer().getId());
        assertEquals(1, updatedOrder.getOrderDetails().size());
        assertEquals(itemId2, updatedOrder.getOrderDetails().getFirst().getItem().getId());
    }

    @Test
    void shouldGetOrdersByCustomerId() {
        List<Order> orders = orderService.getOrdersByCustomerId(customerId1);

        assertThat(orders).hasSize(2);
        for (Order order : orders) {
            assertEquals(customerId1, order.getCustomer().getId());
        }
    }

    @Test
    void shouldRetrieveAllOrders() {
        List<Order> orders = orderService.searchOrders(null, null, null, null, null, null, null, null, null);

        assertThat(orders).hasSize(3);
    }
}
