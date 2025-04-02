package com.ordersapp.core.repository;


import com.ordersapp.core.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

        @Query("SELECT o FROM Order o JOIN FETCH o.customer c LEFT JOIN FETCH o.payment p WHERE o.date >= :startDate AND o.date <= :endDate")
        List<Order> findOrdersByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

        @Query("SELECT o FROM Order o JOIN FETCH o.customer c LEFT JOIN FETCH o.payment p WHERE (:paymentType IS NULL OR TYPE(p) = :paymentType)")
        List<Order> findOrdersByPaymentType(String paymentType);

        List<Order> findOrdersByStatus(String orderStatus);

        List<Order> findOrdersByCustomerName(String customerName);

        @Query("SELECT o FROM Order o JOIN FETCH o.customer c LEFT JOIN FETCH o.payment p WHERE (:city IS NULL OR c.address.city = :city) AND (:street IS NULL OR c.address.street = :street) AND (:zipcode IS NULL OR c.address.zipcode = :zipcode)")
        List<Order> findOrdersByAddress(String city, String street, String zipcode);

        @Query("SELECT o FROM Order o JOIN FETCH o.customer c LEFT JOIN FETCH o.payment p WHERE (:paymentStatus IS NULL OR (:paymentStatus = 'PAID' AND p.amount > 0) OR (:paymentStatus = 'PENDING' AND (p IS NULL OR p.amount = 0)))")
        List<Order> findOrdersByPaymentStatus(@Param("paymentStatus") String paymentStatus);

}