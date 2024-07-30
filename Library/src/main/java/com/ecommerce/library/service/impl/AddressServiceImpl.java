package com.ecommerce.library.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.library.model.Address;
import com.ecommerce.library.repository.AddressRepository;
import com.ecommerce.library.service.AddressService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressServiceImpl implements AddressService {

    @Autowired
    public AddressRepository addressRepository;

    @Override
    public Address save(Address address) {
        
        Address address2 = addressRepository.save(address);
        return address2;
    }

    @Override
    public Address getAddressByCustomerId(Long id) {
        
        Address a =  addressRepository.findAddressByCustomerId(id);
        return a;
    }

    
} 
