package com.banm.abb.StocksApp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "stocks_purchased")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StocksPurchased {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long ownerId;

    private Long itemId;

    private int count;
}
