//package com.ecommerce.admin.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.ecommerce.library.model.Order;
//import com.ecommerce.library.repository.OrderRepository;
//import com.ecommerce.library.service.InvoiceReportService;
//
//import lombok.RequiredArgsConstructor;
//
//import java.util.List;
//
//@RequiredArgsConstructor
//@Controller
//public class InvoiceController {
//
//    @Autowired
//    private final InvoiceReportService invoiceReportService;
//    private final OrderRepository orderRepository;
//
//    @GetMapping("/generate-invoice-report")
//    public ResponseEntity<?> generateInvoiceReport() {
//        try {
//
//            List<Order> orders = orderRepository.findAll();
//            // Call the InvoiceReportService to generate the PDF report
//            invoiceReportService.generateInvoicePDF(orders);
//            return ResponseEntity.ok("Invoice report generated successfully");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating invoice report");
//        }
//    }
//
//}
