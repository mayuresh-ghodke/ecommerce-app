package com.ecommerce.library.service.impl;

import com.ecommerce.library.model.PaymentOrder;
import com.ecommerce.library.repository.PaymentOrderRepository;
import com.ecommerce.library.service.PaymentOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentOrderServiceImpl implements PaymentOrderService{

    @Autowired
    private PaymentOrderRepository paymentOrderRepository;

    @Override
    public PaymentOrder save(PaymentOrder paymentOrder) {
        return paymentOrderRepository.save(paymentOrder);
    }
    
}
