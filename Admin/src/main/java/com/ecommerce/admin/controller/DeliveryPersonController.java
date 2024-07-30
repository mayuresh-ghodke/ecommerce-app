package com.ecommerce.admin.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.ecommerce.library.model.DeliveryPerson;
import com.ecommerce.library.service.DeliveryPersonService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class DeliveryPersonController {

    private final DeliveryPersonService deliveryPersonService;

    @GetMapping("/do-login-delivery-person")
    public void getLogin(){
        System.out.println("Called here");
    }

    @GetMapping("/add-delivery-person")
    public String addDeliveryPerson(Model model) {

        DeliveryPerson deliveryPerson = new DeliveryPerson();

        model.addAttribute("deliveryPerson", deliveryPerson);
        model.addAttribute("title", "Add Delivery Person");

        return "add-delivery-person";
    }

    @PostMapping("/save-delivery-person")
    public String saveDeliveryPerson(@ModelAttribute("deliveryPerson") DeliveryPerson deliveryPerson,
            Model model) {

        deliveryPerson.setAvailable(true);
        deliveryPersonService.createDeliveryPerson(deliveryPerson);
        model.addAttribute("title", "Add Delivery Person");
        return "add-delivery-person";
    }

    @GetMapping("/delivery-persons")
    public String getAllDeliveryPersons(Model model, Principal principal) {

        if (principal == null) {
            return "redirect:/login";
        }
        List<DeliveryPerson> deliveryPersonsList = deliveryPersonService.getAllDeliveryPersons();

        model.addAttribute("deliveryPersons", deliveryPersonsList);
        model.addAttribute("size", deliveryPersonsList.size());
        model.addAttribute("title", "View Delivery Persons");
        return "delivery-persons";
    }

    

}
