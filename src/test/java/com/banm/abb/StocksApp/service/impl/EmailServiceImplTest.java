package com.banm.abb.StocksApp.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {
    @InjectMocks
    private EmailServiceImpl service;

    @Mock
    @SuppressWarnings("unused")
    private JavaMailSender mailSender;

    @Test
    void sendMail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("mohsun.babayev.2003@gmail.com");
        message.setSubject("Test email");
        message.setFrom("stocksappsmtpmohsun@gmail.com");
        message.setText("This is a test email.");
        assertDoesNotThrow(() -> service.sendMail(message));
    }
}
