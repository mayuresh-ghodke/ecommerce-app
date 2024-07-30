package com.ecommerce.customer.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.model.Wishlist;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.ProductService;
import com.ecommerce.library.service.WishlistService;

import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class WishlistController {

    private final ProductService productService;
    private final WishlistService wishlistService;
    private final CustomerService customerService;

    @PostMapping("/add-to-wishlist/{id}")
    public ResponseEntity<String> addToWishlist(@PathVariable("id") Long id, Principal principal) {
        if (principal == null) {
            // If the user is not logged in, return a 401 Unauthorized status
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not logged in.");
        }

        Product product = productService.findById(id);

        String username = principal.getName();
        Long customerId = customerService.getCustomerId(username);
        Customer customer = customerService.getCustomerById(customerId);

        Wishlist existingWishlistEntry = wishlistService.getByCustomerIdAndProductId(customerId, product.getId());
        if (existingWishlistEntry != null) {
            System.out.println("Proudct is already added to cart.");
            String message = existingWishlistEntry.getProduct().getName() + " already added to wishlist.";
            return ResponseEntity.ok().body(
                    "{\"message\": \"" + message + "\"}");
        }
        Wishlist wishlist = new Wishlist();
        wishlist.setCustomer(customer);
        wishlist.setProduct(product);

        Wishlist createdWishlist = wishlistService.save(wishlist);

        String message = createdWishlist.getProduct().getName() + " added to wishlist.";
        // JSON response with a success message
        return ResponseEntity.ok().body(
                "{\"message\": \"" + message + "\"}");

    }

    @GetMapping("/wishlist")
    public String wishlist(Principal principal, Model model) {

        if (principal == null) {
            return "redirect:/login";
        }
        String username = principal.getName();
        Long customerId = customerService.getCustomerId(username);

        List<Wishlist> wishlist = wishlistService.getWishlistByCustomerId(customerId);
        List<Product> products = new ArrayList<>();
        for (Wishlist wl : wishlist) {
            products.add(wl.getProduct());
        }
        model.addAttribute("size", products.size());
        model.addAttribute("products", products);
        model.addAttribute("title", "Wishlist");
        return "wishlist";
    }

    @GetMapping("/remove-from-wishlist/{id}")
    public ResponseEntity<String> deleterProduct(@PathVariable("id") Long id, Principal principal, Model model) {
        String username = principal.getName();
        Long customerId = customerService.getCustomerId(username);
        
        Wishlist wishlist = wishlistService.getByCustomerIdAndProductId(customerId, id);
        if (wishlist != null) {
            // Delete the item from the wishlist
            String message = wishlist.getProduct().getName()+" removed from wishlist.";
            boolean result = wishlistService.deleteById(wishlist.getId());
            return ResponseEntity.ok().body(
                "{\"message\": \"" + message + "\"}");
        } else {
            // If the item is not found in the wishlist, return a not found response
            return ResponseEntity.notFound().build();
        }
    }

}
