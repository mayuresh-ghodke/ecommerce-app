package com.ecommerce.customer.controller;

import com.ecommerce.library.dto.CustomerDto;
import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.*;
import com.ecommerce.library.repository.OrderDetailRepository;
import com.ecommerce.library.repository.OrderRepository;
import com.ecommerce.library.service.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final CustomerService customerService;
    private final OrderService orderService;
    private final ProductService productService;
    private final EmailSenderService emailSenderService;

    @Autowired
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    private final AddressService addressService;

    private final ReviewService reviewService;

    @GetMapping("/check-out")
    public String checkOut(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            CustomerDto customer = customerService.getCustomer(principal.getName());

            Long id = customerService.getCustomerId(customer.getUsername());
            Address address = addressService.getAddressByCustomerId(id);
            if(address==null){
                model.addAttribute("page", "profile");
                return "redirect:/profile";
            }
            else{
                ShoppingCart cart = customerService.findByUsername(principal.getName()).getCart();
            model.addAttribute("customer", customer);
            customer.setAddress(address.getStreet());
            model.addAttribute("address", address);
            model.addAttribute("title", "Check-Out");
            model.addAttribute("page", "Check-Out");
            model.addAttribute("shoppingCart", cart);
            model.addAttribute("grandTotal", cart.getTotalItems());
            return "checkout";
            }
        }
    }

    // include orderDetails in the model
    @GetMapping("/orders")
    public String getOrders(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            Customer customer = customerService.findByUsername(principal.getName());
            List<Order> orderList = customer.getOrders();

            // Create a list to store products
            List<List<Product>> productsList = new ArrayList<>();
            // Create a list to store orderDetails
            List<List<OrderDetail>> orderDetailsList = new ArrayList<>();

            // Loop through each order
            for (Order order : orderList) {
                Long id = order.getId();

                // Retrieve products for the current order
                List<Product> productList = orderDetailRepository.getProductsByOrderId(id);
                // Add products to the list
                productsList.add(productList);

                // Retrieve order details for the current order
                List<OrderDetail> orderDetails = new ArrayList<>();
                for (Product product : productList) {
                    Long productId = product.getId();
                    int quantity = orderDetailRepository.getQuantityByProductIdAndOrderId(productId, order.getId());
                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setProductQuantity(quantity);
                    orderDetails.add(orderDetail); // Add order detail to the list
                }
                orderDetailsList.add(orderDetails); // Add order details list to the main list
            }

            Address address = addressService.getAddressByCustomerId(customer.getId());

            // Add orders, products, and orderDetails to the model
            model.addAttribute("orders", orderList);
            model.addAttribute("address", address);
            model.addAttribute("productsList", productsList);
            model.addAttribute("orderDetailsList", orderDetailsList); // Add orderDetails to the model
            model.addAttribute("title", "View Orders");
            model.addAttribute("page", "order");

            return "order";
        }
    }

    @GetMapping("/cancel-order/{id}")
    public String cancelOrder(@PathVariable("id") Long id, RedirectAttributes attributes) {

        boolean flag = orderService.cancelOrder(id);
        System.out.println("Flag: " + flag);
        if (flag) {
            attributes.addFlashAttribute("success", "Cancel order successfully!");
        } else {
            attributes.addFlashAttribute("error", "Error cancelling order!");
        }
        return "redirect:/orders";
    }

    @RequestMapping(value = "/add-order", method = { RequestMethod.POST })
    public String createOrder(Principal principal,
            @RequestParam("paymentId") String paymentId,
            @RequestParam("status") String status,
            Model model,
            HttpSession session) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            Customer customer = customerService.findByUsername(principal.getName());
            ShoppingCart cart = customer.getCart();
            Order order = orderService.save(cart);

            // We set payemntId and status after saving order
            order.setPaymentId(paymentId);
            order.setStatus(status);
            System.out.println("--------");
            System.out.println("Status: "+status); 
            orderRepository.save(order);

            // send email to customer about order confirmation
            String toEmail = customer.getUsername();
            String subject = "Order Confirmation";
            String firstName = customer.getFirstName();
            String lastName = customer.getLastName();

            List<Product> productsList = orderDetailRepository.getProductsByOrderId(order.getId());

            StringBuilder productDetails = new StringBuilder();

            for (Product product : productsList) {
                Long productId = product.getId();
                int quantity = orderDetailRepository.getQuantityByProductIdAndOrderId(productId, order.getId());
                double unitPrice = product.getCostPrice(); // Assuming the Product class has a method to get the unit
                                                           // price
                double totalPrice = quantity * unitPrice;

                // Add product details to the message
                productDetails.append("Product: ").append(product.getName())
                        .append(" | Qty: ").append(quantity)
                        .append(" | Unit Price: ").append(unitPrice)
                        .append(" | Total: ").append(totalPrice)
                        .append("\n");
            }

            // Construct the final message body
            String body = "Mr/Mrs. " + firstName + " " + lastName + " your order has been placed successfully. " +
                    "OrderId = " + order.getId() +
                    " | Total QTY = " + order.getQuantity() +
                    " | Total Amount = " + order.getTotalPrice() + "\n" +
                    "Order Details:\n" + productDetails.toString();

            System.out.println(body); // Display the message or send it via email/SMS

            emailSenderService.sendSimpleEmail(toEmail, body, subject);

            session.removeAttribute("totalItems");
            model.addAttribute("order", order);
            model.addAttribute("title", "Order Detail");
            model.addAttribute("page", "order-detail");
            model.addAttribute("success", "Order has been placed successfully");
            return "order-detail";
        }
    }

    // View Receipt
    @GetMapping("/view-order-receipt/{id}")
    public String getOrderReceipt(@PathVariable("id") Long id,
            Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            Customer customer = customerService.findByUsername(principal.getName());
            Address address = addressService.getAddressByCustomerId(customer.getId());

            Order order = orderService.getOrderByOrderId(id);

            List<List<OrderDetail>> orderDetailsList = new ArrayList<>();

            List<Product> productsList = orderDetailRepository.getProductsByOrderId(order.getId());

            // to get and display quantity
            // Retrieve order details for the current order
            List<OrderDetail> orderDetails = new ArrayList<>();
            for (Product product : productsList) {
                Long productId = product.getId();
                int quantity = orderDetailRepository.getQuantityByProductIdAndOrderId(productId, order.getId());
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setProductQuantity(quantity);
                orderDetails.add(orderDetail); // Add order detail to the list
            }
            orderDetailsList.add(orderDetails);

            // Add orders and products to the model
            model.addAttribute("orders", order);
            model.addAttribute("orderDetails", orderDetails);
            model.addAttribute("address", address);
            model.addAttribute("customer", customer);
            model.addAttribute("productsList", productsList);
            model.addAttribute("orderDetailsList", orderDetailsList);
            model.addAttribute("title", "View Receipt");
            model.addAttribute("page", "Order");

            return "view-receipt";
        }
    }

    @GetMapping("/trackOrder")
    public String getOrderTracking(Model model,
            @RequestParam("orderId") Long orderId,
            @RequestParam("phoneNumber") String phoneNumber,
            Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        String username = principal.getName();
        Customer customer = customerService.findByUsername(username);
        System.out.println(phoneNumber + " " + customer.getPhoneNumber());
        Order order = orderService.getOrderByOrderId(orderId);
        if (order == null) {
            model.addAttribute("status",
                    "Order with ID-" + orderId + " Not found for user with mobile number." + phoneNumber);
            return "track-order";
        }

        if (order.getCustomer().getPhoneNumber().equalsIgnoreCase(phoneNumber)) {
            System.out.println("-----------");
            if (order.isAccept()) {
                Date deliveryDate = order.getDeliveryDate();
                Date currentDate = new Date(); // Current date

                if (currentDate.after(deliveryDate)) {
                    model.addAttribute("status", "Your order has been delivered on ." + deliveryDate);
                } else {
                    model.addAttribute("status",
                            "Your order is accepted.\n" + "Order will be delivered on " + deliveryDate);
                }
            } else {
                model.addAttribute("status", "Order is pending. Order placed on " + order.getOrderDate());
            }
        } else {
            model.addAttribute("status", "Mobile Number not matched.");
        }
        model.addAttribute("page", "track-order");
        model.addAttribute("title", "Track Order");
        return "track-order";
    }

    @GetMapping("/write-review/{id}/{prodId}")
    public String getWriteReview(@PathVariable("id") Long id,
            @PathVariable("prodId") Long prodId,
            Model model, Principal principal) {
        Customer customer = customerService.findByUsername(principal.getName());
        Order order = orderService.getOrderByOrderId(id);

        List<Product> productsList = orderDetailRepository.getProductsByOrderId(order.getId());
        List<OrderDetail> orderDetails = new ArrayList<>();

        for (Product product : productsList) {
            Long productId = product.getId();
            int quantity = orderDetailRepository.getQuantityByProductIdAndOrderId(productId, order.getId());
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setProductQuantity(quantity);
            orderDetails.add(orderDetail);
        }

        ProductDto productDto = productService.getById(prodId);
        Product product = new Product();
        product.setName(productDto.getName());

        Review review = reviewService.getReviewByCustomerIdAndProductId(customer.getId(), prodId);

        if (review != null) {
            model.addAttribute("review", review);
        } else {
            Review review2 = new Review();
            review2.setProductId(prodId);
            model.addAttribute("review", review2);
        }
        model.addAttribute("orders", order);
        model.addAttribute("orderDetails", orderDetails);
        model.addAttribute("customer", customer);
        model.addAttribute("productsList", productsList);
        model.addAttribute("product", product);
        model.addAttribute("title", "Write Review");

        return "write-review";
    }

    // to filters order by status
    @GetMapping("/get-orders-by-status")
public String filterOrders(@RequestParam("status") String orderStatus, Model model, Principal principal) {
    if (principal == null) {
        return "redirect:/login";
    }
    
    String username = principal.getName();
    Long customerId = customerService.getCustomerId(username);
    Customer customer = customerService.getCustomerById(customerId);
    
    // Assuming you have a method to get orders by customer ID
    List<Order> ordersList = customer.getOrders();

    // Debug output to verify statuses
    System.out.println("--------------------");
    for (Order order : ordersList) {
        System.out.println(order.getOrderStatus());
    }
    
    // Filter orders by status using equals method
    List<Order> filteredOrders = ordersList.stream()
        .filter(order -> orderStatus.equals(order.getOrderStatus()))
        .collect(Collectors.toList());

    // Debug output to verify statuses
    System.out.println("--------------------");
    for (Order order : filteredOrders) {
        System.out.println(order.getStatus());
    }
    
    model.addAttribute("title", "Orders");
    model.addAttribute("orders", filteredOrders); // Add filtered orders to the model
    return "order";
}



}
