package com.banm.abb.StocksApp.service.impl;


import com.banm.abb.StocksApp.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendMail(SimpleMailMessage mail) {
        javaMailSender.send(mail);
    }
}
