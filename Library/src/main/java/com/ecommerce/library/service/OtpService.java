package com.ecommerce.library.service;

public interface OtpService {

    public void generateAndSendOtp(String email);

    public void sendOtpEmail(String email, String otp);

    public boolean verifyOtp(String otp);
    
}
