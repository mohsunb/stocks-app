package com.banm.abb.StocksApp.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class StocksAvailableDto {

    private String name;
    private BigDecimal price;
    private int availableCount;
}
