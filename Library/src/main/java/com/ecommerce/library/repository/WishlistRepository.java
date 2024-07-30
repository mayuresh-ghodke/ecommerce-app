package com.ecommerce.library.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.library.model.Product;
import com.ecommerce.library.model.Wishlist;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long>{
    
    @SuppressWarnings("unchecked")
    Wishlist save(Wishlist wishlist);

    List<Wishlist> findAllByCustomerId(Long customerId);

    List<Product> findAllProductsByCustomerId(Long customerId);

    Wishlist findByCustomerIdAndProductId(Long customerId, Long productId);

}
