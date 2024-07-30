package com.ecommerce.library.repository;

import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Order;
import com.ecommerce.library.model.OrderDetail;
import com.ecommerce.library.model.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    @Query("select o from Order o where o.customer.id = ?1")
    List<Order> findAllByCustomerId(Long id);

    // @Query("SELECT od.product FROM OrderDetail od WHERE od.id = ?1")
    // ProductDto getProductByOrderDetailId(Long orderDetailId);

    // @Query("SELECT od.order FROM OrderDetail od WHERE od.id = ?1")
    // Order getOrderByOrderDetailId(Long orderDetailId);

    @Query("SELECT od.product FROM OrderDetail od WHERE od.order.id = ?1")
    List<Product> getProductsByOrderId(Long orderId);

    @Query("SELECT od.product FROM OrderDetail od WHERE od.order.id = ?1")
    Product getProductByOrderId(Long orderId);

    @Query("SELECT od.productQuantity FROM OrderDetail od WHERE od.product.id = :productId AND od.order.id = :orderId")
    int getQuantityByProductIdAndOrderId(Long productId, Long orderId);


}
