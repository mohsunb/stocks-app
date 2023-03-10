package com.banm.abb.StocksApp.service.impl;


import com.banm.abb.StocksApp.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;

    public void sendMail(SimpleMailMessage mail) {
        javaMailSender.send(mail);
    }
}
