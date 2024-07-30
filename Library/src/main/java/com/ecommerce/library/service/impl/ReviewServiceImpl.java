package com.ecommerce.library.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.library.model.Review;
import com.ecommerce.library.repository.ReviewRepository;
import com.ecommerce.library.service.ReviewService;

import java.util.List;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{

    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public Review save(Review review) {
        
        return reviewRepository.save(review);

    }

    @Override
    public Review update(Review review) {
        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getReviewsByCustomerId(Long id) {
        return reviewRepository.findReviewsByCustomerId(id);
    }

    @Override
    public Review getReviewByCustomerIdAndProductId(Long customerId, Long productId) {
        
        return reviewRepository.findReviewByCustomerIdAndProductId(customerId, productId);
    }

    @Override
    public List<Review> getReviewsByProductId(Long productId) {
        
        return reviewRepository.findReviewsByProductId(productId);
    }

    @Override
    public List<Review> getAllReviews() {
        
        return reviewRepository.findAll();
    }

    @Override
    public Review getReviewByReviewId(Long reviewId) {
        
        Review review = reviewRepository.findReviewByReviewId(reviewId);
        return review;
    }

    @Override
    public boolean deleteReviewById(Long reviewId) {
        
        reviewRepository.deleteById(reviewId);

        return true;
    }
    
}
