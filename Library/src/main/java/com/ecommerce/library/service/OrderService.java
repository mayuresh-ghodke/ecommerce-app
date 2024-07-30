package com.ecommerce.library.service;

import com.ecommerce.library.model.DeliveryPerson;
import com.ecommerce.library.model.Order;
import com.ecommerce.library.model.ShoppingCart;

import java.util.List;


public interface OrderService {
    Order save(ShoppingCart shoppingCart);

    List<Order> findAll(String username);

    List<Order> findALlOrders();

    Order acceptOrder(Long id);

    boolean cancelOrder(Long id);

    Order assignDeliveryPerson(Long orderId, DeliveryPerson deliveryPerson);

    //Order updatePaymentIdandStatus(Long id);

    Order getOrderByOrderId(Long id);

    void updateDeliveryStatusAutomatically();

    List<Order> getOrdersByCustomerIdAndOrderStatus(Long customerId, String status);

    List<Order> getAllPendingOrders();

    List<Order> getAllDeliveredOrders();

    List<Order> getAllAcceptedOrders();

}
