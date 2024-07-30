package com.ecommerce.library.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "payment_order")
public class PaymentOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long paymentOrderId;

    //orderId is the order id of the razorpay payement, that is receive from razorpay
    private String orderId;

    private String amount;

    private String receipt;

    private String status;

    @ManyToOne
    private Customer customer;

    private String paymentId;

    //payment

    // Parameterized Constructor
    public PaymentOrder(String orderId, String amount, String receipt, String status, Customer customer,
            String paymentId) {
        this.orderId = orderId;
        this.amount = amount;
        this.receipt = receipt;
        this.status = status;
        this.customer = customer;
        this.paymentId = paymentId;
    }

    // Getter and Setter methods

    public PaymentOrder() {
        // TODO Auto-generated constructor stub
    }

    public Long getPaymentOrderId() {
        return paymentOrderId;
    }

    public void setMyPaymentId(Long paymentOrderId) {
        this.paymentOrderId = paymentOrderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }
}
