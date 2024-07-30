package com.ecommerce.library.service;

import com.ecommerce.library.model.PaymentOrder;
import org.springframework.stereotype.Service;

@Service
public interface PaymentOrderService {

    PaymentOrder save(PaymentOrder paymentOrder);
    
}
