package com.ecommerce.library.repository;

import com.ecommerce.library.model.Order;

import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean deleteOrderById(Long orderId);

    Order save(Order order);

    @Transactional
    @Modifying
    @Query("UPDATE Order o SET o.paymentId = :paymentId, o.status = :status WHERE o.id = :orderId")
    Order updatePaymentIdAndStatus(Long orderId, String paymentId, String status);

    Order findOrderById(Long id);

    List<Order> findOrdersByCustomerIdAndOrderStatus(Long customerId, String orderStatus);

}
