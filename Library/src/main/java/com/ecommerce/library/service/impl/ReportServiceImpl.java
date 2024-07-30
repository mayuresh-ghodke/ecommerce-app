package com.ecommerce.library.service.impl;

import java.util.List;

import com.ecommerce.library.model.Order;
import com.ecommerce.library.repository.OrderRepository;
import com.ecommerce.library.service.ReportService;

import jakarta.servlet.http.HttpServletResponse;

public class ReportServiceImpl implements ReportService{

    private OrderRepository orderRepository;
    @Override
    public void generateExcel(HttpServletResponse httpServletResponse) {
        
        List<Order> orders = orderRepository.findAll();



    }
    
}
