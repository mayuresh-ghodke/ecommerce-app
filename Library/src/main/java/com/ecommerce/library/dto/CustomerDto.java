package com.ecommerce.library.dto;

import com.ecommerce.library.model.City;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {
    @NotEmpty(message = "Firstname is required.")
    @Size(min = 3, max = 16, message = "First name contains 3-16 characters")
    private String firstName;

    @NotEmpty(message = "Lastname is required.")
    @Size(min = 3, max = 10, message = "Last name contains 3-10 characters")
    private String lastName;
    
    @NotEmpty(message = "Email is required.")
    @Email
    private String username;

    @NotEmpty(message = "Password is required.")
    @Size(min = 3, max = 15, message = "Password contains 3-10 characters")
    private String password;

    @Size(min = 10, max = 15, message = "Phone number contains 10-15 characters")
    private String phoneNumber;

    private String address;
    private String confirmPassword;
    private City city;
    private String image;
    private String country;

}
