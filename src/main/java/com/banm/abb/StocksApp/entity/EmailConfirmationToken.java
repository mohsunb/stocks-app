package com.banm.abb.StocksApp.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "confirmation_tokens")
@Data
@NoArgsConstructor
public class EmailConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String token;
    private Date creationDate;

    public EmailConfirmationToken(String username) {
        this.username = username;
        this.token = UUID.randomUUID().toString();
        this.creationDate = new Date();
    }
}
