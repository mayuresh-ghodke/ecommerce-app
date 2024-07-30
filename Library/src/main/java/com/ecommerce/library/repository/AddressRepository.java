package com.ecommerce.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.library.model.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long>{
    
    @SuppressWarnings("unchecked")
    Address save(Address address);

    public Address findAddressByCustomerId(Long Id);
}
