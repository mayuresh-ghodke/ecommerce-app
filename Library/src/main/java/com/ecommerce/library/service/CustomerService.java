package com.ecommerce.library.service;

import java.util.List;

import com.ecommerce.library.dto.CustomerDto;
import com.ecommerce.library.model.Customer;

public interface CustomerService {
    Customer save(CustomerDto customerDto);

    Customer findByUsername(String username);

    Customer update(CustomerDto customerDto);

    Customer changePass(CustomerDto customerDto);

    CustomerDto getCustomer(String username);

    List<Customer> getAllCustomers();

    Long getCustomerId(String username);

    Customer getCustomerById(Long id);

    boolean deleteById(Long Id);
}
