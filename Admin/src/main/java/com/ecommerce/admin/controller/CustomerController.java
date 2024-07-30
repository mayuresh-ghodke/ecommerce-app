package com.ecommerce.admin.controller;

import lombok.RequiredArgsConstructor;

import java.security.Principal;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecommerce.library.dto.CustomerDto;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.service.CustomerService;

@Controller
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/customers")
    public String customers(Model model){
        List<Customer> customerList = customerService.getAllCustomers();
        model.addAttribute("customers", customerList);
        model.addAttribute("title","Customers");
        model.addAttribute("size",customerList.size());
        return "customers";
    }

    @GetMapping("/update-customer/{id}")
    public String updateCustomer(@PathVariable("id") Long id,
         Model model, Principal principal){
        if (principal == null) {
            return "redirect:/login";
        }
        Customer customer = customerService.getCustomerById(id);
        model.addAttribute("customer", customer);
        model.addAttribute("title", "Update Customer");        
        return "update-customer";
    }

    @PostMapping("/update-customer/{id}")
    public String updateCustomerInfo(@ModelAttribute("customer")Customer customer,
         Model model, RedirectAttributes redirectAttributes,Principal principal){
        
            try{
                if (principal == null) {
                    return "redirect:/login";
                }
                CustomerDto customerDto = new CustomerDto();
                customerDto.setFirstName(customer.getFirstName());
                customerDto.setLastName(customer.getLastName());
                customerDto.setPhoneNumber(customer.getPhoneNumber());
                customerDto.setUsername(customer.getUsername());
        
                customerService.update(customerDto);
                redirectAttributes.addFlashAttribute("success", "Update successfully!");
            }

            catch(Exception e){
                System.out.println("Error: "+e);
                redirectAttributes.addFlashAttribute("error", "Oops! Something Went Wrong.");
            }

        
        return "redirect:/customers";
    }

    @GetMapping("/delete-customer/{id}")
    public String deleteCustomer(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            boolean result = customerService.deleteById(id);
            System.out.println("Result: "+result);
            redirectAttributes.addFlashAttribute("success", "Customer Deleted successfully!");
        } catch (DataIntegrityViolationException e1) {
            e1.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error: please check again!");
        } catch (Exception e2) {
            e2.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error on server");
        }
        return "redirect:/customers";
    }
}
