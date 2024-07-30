package com.ecommerce.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.library.model.DeliveryPerson;

public interface DeliveryPersonRepository extends JpaRepository<DeliveryPerson, Long>{
    
    DeliveryPerson findDeliveryPersonById(Long deliveryPersonId);
}
