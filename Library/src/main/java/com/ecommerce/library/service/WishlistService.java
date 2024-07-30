package com.ecommerce.library.service;

import java.util.List;

import com.ecommerce.library.model.Product;
import com.ecommerce.library.model.Wishlist;

public interface WishlistService {

    Wishlist save(Wishlist wishlist);

    List<Wishlist> getWishlistByCustomerId(Long customerId);

    Wishlist getByCustomerIdAndProductId(Long customerId, Long productId);

    List<Product> getAllProductsByCustomerId(Long customerId);

    boolean deleteById(Long wishlistId);
    
} 
