package com.ecommerce.library.service;

import com.ecommerce.library.model.Address;

public interface AddressService {

    public Address save(Address address);

    public Address getAddressByCustomerId(Long id);
    
} 