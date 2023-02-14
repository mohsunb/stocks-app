package com.banm.abb.StocksApp.service;


import org.springframework.mail.SimpleMailMessage;

public interface EmailService {

    void sendMail(SimpleMailMessage mail);
}
