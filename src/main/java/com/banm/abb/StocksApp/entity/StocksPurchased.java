package com.banm.abb.StocksApp.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class StocksPurchased {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long ownerId;

    private int count;
}
