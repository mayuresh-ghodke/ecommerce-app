package com.ecommerce.admin.controller;

import com.ecommerce.library.model.DeliveryPerson;
import com.ecommerce.library.model.Order;
import com.ecommerce.library.service.DeliveryPersonService;
import com.ecommerce.library.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.io.IOException;
import java.util.List;

import java.awt.Color;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final DeliveryPersonService deliveryPersonService;

    @GetMapping("/orders")
    public String getAll(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        } 
        else
        {
            List<Order> orderList = orderService.findALlOrders();

            orderService.updateDeliveryStatusAutomatically();
            
            List<DeliveryPerson> deliveryPersons = deliveryPersonService.getAllDeliveryPersons();
            
            model.addAttribute("deliveryPersons", deliveryPersons);
            model.addAttribute("orders", orderList);
            model.addAttribute("title", "Manage Orders");
            return "orders";
        }
    }

    @RequestMapping(value = "/assign-delivery-person/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
    public String assignDeliveryPerson(@PathVariable("id") Long id, 
        @RequestParam("delivery_person") Long deliveryPersonId, 
        Principal principal,
        Model model){

        if(principal == null){
            return "redirect:/login";
        }
        DeliveryPerson deliveryPerson = deliveryPersonService.getDeliveryPerson(deliveryPersonId);
        Order order = orderService.getOrderByOrderId(id);
        order.setDeliveryPerson(deliveryPerson);

        orderService.assignDeliveryPerson(id, deliveryPerson);

        List<Order> orderList = orderService.findALlOrders();
            
        List<DeliveryPerson> deliveryPersons = deliveryPersonService.getAllDeliveryPersons();

        model.addAttribute("deliveryPersons", deliveryPersons);
        model.addAttribute("orders", orderList);
        model.addAttribute("title", "Manage Orders");
        return "orders";
    }


    @RequestMapping(value = "/accept-order", method = {RequestMethod.PUT, RequestMethod.GET})
    public String acceptOrder(Long id, RedirectAttributes attributes, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            orderService.acceptOrder(id);
            attributes.addFlashAttribute("success", "Order Accepted");
            return "redirect:/orders";
        }
    }

    @RequestMapping(value = "/cancel-order", method = {RequestMethod.PUT, RequestMethod.GET})
    public String cancelOrder(Long id, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            orderService.cancelOrder(id);
            return "redirect:/orders";
        }
    }

    // to generate pdf report
    public void generatePDFReport(List<Order> orders) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.beginText();
                contentStream.newLineAtOffset(100, 700);
                contentStream.showText("Invoice Report");
                contentStream.newLineAtOffset(0, -20);

                // Loop through orders and write them to PDF
                for (Order order : orders) {
                    contentStream.showText("Order ID: " + order.getId());
                    // Add more fields as needed
                    contentStream.newLineAtOffset(0, -20);
                }

                contentStream.endText();
            }

            document.save("Invoice_Report.pdf");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    


}
