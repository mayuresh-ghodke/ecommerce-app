package com.ecommerce.library.service.impl;

import com.ecommerce.library.model.*;
import com.ecommerce.library.repository.CustomerRepository;
import com.ecommerce.library.repository.OrderDetailRepository;
import com.ecommerce.library.repository.OrderRepository;
import com.ecommerce.library.service.OrderService;
import com.ecommerce.library.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository detailRepository;
    private final CustomerRepository customerRepository;
    private final ShoppingCartService cartService;

    @Override
    @Transactional
    public Order save(ShoppingCart shoppingCart) {
        Order order = new Order();
        order.setOrderDate(new Date());
        order.setCustomer(shoppingCart.getCustomer());
        order.setTax(0);
        order.setTotalPrice(shoppingCart.getTotalPrice());
        order.setAccept(false);
        order.setPaymentMethod("Online");
        order.setOrderStatus("Pending");
        order.setQuantity(shoppingCart.getTotalItems());
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (CartItem item : shoppingCart.getCartItems()) {
            // order.setProdId(item.getProduct().getId());
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(item.getProduct());

            int currentquantity = item.getProduct().getCurrentQuantity();
            int quantity = item.getQuantity();// Quantity in cart item of pertiular product
            int result = currentquantity - quantity;

            item.getProduct().setCurrentQuantity(result);

            // here we set quantity of products in cart into order detail table
            orderDetail.setProductQuantity(item.getQuantity());

            detailRepository.save(orderDetail);
            orderDetailList.add(orderDetail);
        }
        order.setOrderDetailList(orderDetailList);
        cartService.deleteCartById(shoppingCart.getId());
        return orderRepository.save(order);
    }

    @Override
    public List<Order> findAll(String username) {
        Customer customer = customerRepository.findByUsername(username);
        List<Order> orders = customer.getOrders();
        return orders;
    }

    @Override
    public List<Order> findALlOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order acceptOrder(Long id) {
    Order order = orderRepository.getById(id);
    order.setOrderStatus("Accepted");
    order.setAccept(true);

    // Calculate a random delivery date within the next 8 days
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date());
    Random random = new Random();
    int daysToAdd = random.nextInt(8) + 1; // Add 1 to ensure minimum 1 day
    calendar.add(Calendar.DAY_OF_MONTH, daysToAdd);
    Date deliveryDate = calendar.getTime();

    order.setDeliveryDate(deliveryDate);
    return orderRepository.save(order);
    }

    

    @Transactional
    @Override
    public boolean cancelOrder(Long id) {
        try {
            return orderRepository.deleteOrderById(id);
        } catch (Exception e) {

            System.err.println("Error cancelling order: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Order getOrderByOrderId(Long id) {
        Order order = orderRepository.findOrderById(id);
        return order;
    }

    @Override
    public Order assignDeliveryPerson(Long orderId, DeliveryPerson deliveryPerson) {

        Order order = orderRepository.findOrderById(orderId);
        order.setDeliveryPerson(deliveryPerson);

        return orderRepository.save(order);

    }

    // to set the delivered, on current date and delivery date of the orders are match.
    @Override
    public void updateDeliveryStatusAutomatically() {
        Date currentDate = new Date();

        List<Order> orders = orderRepository.findAll();

        for (Order order : orders) {
            Date deliveryDate = order.getDeliveryDate();
            if (deliveryDate != null && (deliveryDate.before(currentDate) || deliveryDate.equals(currentDate))) {
                order.setDelivered(true);
                orderRepository.save(order);
            }
        }
    }

    @Override
    public List<Order> getOrdersByCustomerIdAndOrderStatus(Long customerId, String status) {
        
        return orderRepository.findOrdersByCustomerIdAndOrderStatus(customerId, status);
        
    }

    public List<Order> getAllPendingOrders(){
        List<Order> allOrders = orderRepository.findAll();
        List<Order> pendingOrders = new ArrayList<>();
        for(Order order: allOrders){
            if(!order.isAccept()){
                pendingOrders.add(order);
            }
        }
        return pendingOrders;
    }
    public List<Order> getAllAcceptedOrders(){
        List<Order> allOrders = orderRepository.findAll();
        List<Order> pendingOrders = new ArrayList<>();
        for(Order order: allOrders){
            if(order.isAccept() && !order.isDelivered()){
                pendingOrders.add(order);
            }
        }
        return pendingOrders;
    }
    public List<Order> getAllDeliveredOrders(){
        List<Order> allOrders = orderRepository.findAll();
        List<Order> deliveredOrders = new ArrayList<>();
        for(Order order: allOrders){
            if(order.isDelivered()){
                deliveredOrders.add(order);
            }
        }
        return deliveredOrders;
    }
}
