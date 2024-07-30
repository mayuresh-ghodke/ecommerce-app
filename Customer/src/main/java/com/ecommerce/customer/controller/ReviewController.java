package com.ecommerce.customer.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.model.Review;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.ProductService;
import com.ecommerce.library.service.ReviewService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final CustomerService customerService;
    private final ReviewService reviewService;
    private final ProductService productService;

    // To get all reviews of all products of a customer
    @GetMapping("/reviews")
    public String getReviews(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            Customer customer = customerService.findByUsername(principal.getName());

            List<Review> reviewList = reviewService.getReviewsByCustomerId(customer.getId());
            List<Product> productList = new ArrayList<>();
            Product product = new Product();

            for (Review review : reviewList) {
                Long productId = review.getProductId();
                product = productService.findById(productId);
                productList.add(product);
            }
            model.addAttribute("reviewList", reviewList);
            model.addAttribute("productList", productList);
            model.addAttribute("title", "Reviews");
            model.addAttribute("page", "reviews");
        }

        return "reviews";
    }

    @RequestMapping(value = "/get-review-details/{productId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> getReviewDetails(@PathVariable("productId") Long productId) {
        // Get the current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            // Handle unauthenticated user
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        String username = authentication.getName();
        Long customerId = customerService.getCustomerId(username);
        Review review = reviewService.getReviewByCustomerIdAndProductId(customerId, productId);

        if (review == null) {
            // Handle if review is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Review not found");
        }

        return ResponseEntity.ok(review);
    }

    @PostMapping({ "/create-review", "/update-review" })
    public String createOrUpdateReview(@ModelAttribute("review") Review review,
            Principal principal, Model model,
            RedirectAttributes attributes) {

        Long customerId = customerService.getCustomerId(principal.getName());
        Customer customer = customerService.getCustomerById(customerId);

        Long productId = review.getProductId();

        // Check if a review already exists for the given customer and product
        Review existingReview = reviewService.getReviewByCustomerIdAndProductId(customerId, productId);

        if (existingReview != null) {
            // Update existing review

            existingReview.setRatingNumber(review.getRatingNumber());
            existingReview.setFeedback(review.getFeedback());
            reviewService.save(existingReview);
            model.addAttribute("resultedReview", existingReview);
            attributes.addFlashAttribute("success", "Review Updated Successfully!");
        } else {
            // Create new review
            review.setProductId(productId);
            review.setCustomer(customer);
            Review savedReview = reviewService.save(review);
            model.addAttribute("resultedReview", savedReview);
            attributes.addFlashAttribute("success", "Review Added Successfully!");
        }
        Product product = new Product();
        ProductDto productDto = productService.getById(productId);
        product.setName(productDto.getName());
        model.addAttribute("product", product);
        model.addAttribute("title", "Add Review");

        return "write-review";
    }

    // To update the review
    @GetMapping("/update-review/{reviewId}")
    public String updateReview(@PathVariable("reviewId") Long reviewId,
            Model model, Principal principal, RedirectAttributes attributes) {

        if (principal == null) {
            return "redirect:/login";
        }

        Review review = reviewService.getReviewByReviewId(reviewId);
        System.out.println(review.getProductId() + review.getCustomer().getFirstName());

        Long customerId = customerService.getCustomerId(principal.getName());
        Customer customer = customerService.getCustomerById(customerId);

        Long productId = review.getProductId();

        // Check if a review already exists for the given customer and product
        Review existingReview = reviewService.getReviewByCustomerIdAndProductId(customerId, productId);

        if (existingReview != null) {
            // Update existing review

            existingReview.setRatingNumber(review.getRatingNumber());
            existingReview.setFeedback(review.getFeedback());
            reviewService.save(existingReview);
            model.addAttribute("resultedReview", existingReview);

        }

        Product product = new Product();
        ProductDto productDto = productService.getById(productId);
        product.setName(productDto.getName());
        model.addAttribute("product", product);
        model.addAttribute("review", review);

        model.addAttribute("title", "Update Review");
        attributes.addFlashAttribute("success", "Review Updated Successfully!");
        return "write-review";
    }

    @GetMapping("/delete-review/{reviewId}")
    public String deleteById(@PathVariable("reviewId") Long reviewId,
            Model model, Principal principal) {

        if (principal == null) {
            return "redirect:/login";
        }

        boolean result = reviewService.deleteReviewById(reviewId);

        Customer customer = customerService.findByUsername(principal.getName());

        List<Review> reviewList = reviewService.getReviewsByCustomerId(customer.getId());
        List<Product> productList = new ArrayList<>();
        Product product = new Product();

        for (Review review : reviewList) {
            Long productId = review.getProductId();
            product = productService.findById(productId);
            productList.add(product);
        }
        model.addAttribute("reviewList", reviewList);
        model.addAttribute("productList", productList);
        model.addAttribute("title", "Reviews");
        model.addAttribute("page", "reviews");

        return "reviews";
    }
}
