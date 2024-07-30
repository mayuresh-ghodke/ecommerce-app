package com.ecommerce.admin.controller;

import com.ecommerce.library.model.Review;
import com.ecommerce.library.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/reviews")
    public String products(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        List<Review> reviews = reviewService.getAllReviews();

        for(Review review: reviews){
            System.out.println("Review"+review.getFeedback());
        }
        model.addAttribute("reviews", reviews);
        model.addAttribute("size", reviews.size());
        model.addAttribute("title", "Manage Reviews");

        model.addAttribute("reviews", reviews);
        return "reviews";
    }

    @GetMapping("/delete-review/{id}")
    public String deleteReview(@PathVariable("id") Long reviewId,
        Model model){

            boolean result = reviewService.deleteReviewById(reviewId);
            System.out.println("Review Deleted Status: "+result);
            List<Review> reviewList = reviewService.getAllReviews();

            model.addAttribute("reviews", reviewList);

            return "reviews";
        }
}
