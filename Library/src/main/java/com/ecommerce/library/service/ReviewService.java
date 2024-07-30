package com.ecommerce.library.service;

import java.util.List;

import com.ecommerce.library.model.Review;

public interface ReviewService {

    Review save(Review review); 

    Review update(Review review);

    List<Review> getReviewsByCustomerId(Long id);
    Review getReviewByCustomerIdAndProductId(Long customerId, Long productId);
    List<Review> getReviewsByProductId(Long productId);
    List<Review> getAllReviews();
    Review getReviewByReviewId(Long reviewId);

    boolean deleteReviewById(Long reviewId);
    
}
