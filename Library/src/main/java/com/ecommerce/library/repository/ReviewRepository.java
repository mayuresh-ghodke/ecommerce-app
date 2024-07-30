package com.ecommerce.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.library.model.Review;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>{

    Review findReviewByCustomerIdAndProductId(Long customerId, Long productId);

    List<Review> findReviewsByCustomerId(Long id);

    List<Review> findReviewsByProductId(Long id);

    Review findReviewByReviewId(Long reviewId);
    
} 