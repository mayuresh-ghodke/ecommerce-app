package com.ecommerce.library.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ecommerce.library.model.Product;
import com.ecommerce.library.model.Wishlist;
import com.ecommerce.library.repository.WishlistRepository;
import com.ecommerce.library.service.WishlistService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService{

    private final WishlistRepository wishlistRepository;

    @Override
    public Wishlist save(Wishlist wishlist) {
        return wishlistRepository.save(wishlist);
    }

    @Override
    public List<Product> getAllProductsByCustomerId(Long customerId) {
        List<Product> products = wishlistRepository.findAllProductsByCustomerId(customerId);
    
        return products;
    }

    @Override
    public Wishlist getByCustomerIdAndProductId(Long customerId, Long productId) {
        
        return wishlistRepository.findByCustomerIdAndProductId(customerId, productId);
    }

    @Override
    public List<Wishlist> getWishlistByCustomerId(Long customerId) {
        
        List<Wishlist> wishlist =  wishlistRepository.findAllByCustomerId(customerId);
    
        return wishlist;
    }

    @Override
    public boolean deleteById(Long wishlistId) {
        
        wishlistRepository.deleteById(wishlistId);
        return true;     
    } 
}
