package com.banm.abb.StocksApp.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SellStocksRequestDto {
    private String name;
    private int count;
}
