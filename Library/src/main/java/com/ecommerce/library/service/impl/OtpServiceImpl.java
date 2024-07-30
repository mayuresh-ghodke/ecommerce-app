package com.ecommerce.library.service.impl;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.ecommerce.library.service.OtpService;

@Service
public class OtpServiceImpl implements OtpService{

    private String generatedOtp;

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void generateAndSendOtp(String email) {
        generatedOtp = String.valueOf(new Random().nextInt(999999));
        sendOtpEmail(email, generatedOtp);
        System.out.println("OTP: "+generatedOtp+" send successfully to "+email);
    }

    @Override
    public void sendOtpEmail(String email, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your OTP Code for New Password");
        message.setText("Your OTP code is: " + otp);
        javaMailSender.send(message);
    }

    @Override
    public boolean verifyOtp(String otp) {
        return otp.equals(generatedOtp);
    }
    
}
