package com.ecommerce.library.service;

import java.util.List;

import com.ecommerce.library.model.DeliveryPerson;

public interface DeliveryPersonService {

    DeliveryPerson createDeliveryPerson(DeliveryPerson deliveryPerson);

    DeliveryPerson getDeliveryPerson(Long deliveryPersonId);

    DeliveryPerson updateDeliveryPerson(DeliveryPerson deliveryPerson);

    boolean deleteDeliveryPersonById(Long id);

    List<DeliveryPerson> getAllDeliveryPersons();
    
} 