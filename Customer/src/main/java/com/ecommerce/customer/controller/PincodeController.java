package com.ecommerce.customer.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
@RequiredArgsConstructor
public class PincodeController {

    @PostMapping("/api-pincode/") // Remove the path variable
    public ResponseEntity<?> getPincodeDetails(@RequestParam("pincode") String pincode) { // Use @RequestParam instead of @PathVariable
        try {
            System.out.println("Received pincode: " + pincode);
            // Create RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();

            // Make the HTTP request
            String apiUrl = "https://api.postalpincode.in/pincode/" + pincode;
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(apiUrl, String.class);

            System.out.println("Pincode 1: " + pincode);
            // Check if request was successful
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                // Simply return the response body as it is
                return ResponseEntity.ok(responseEntity.getBody());
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to fetch data from the external service");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
        }
    }
}
