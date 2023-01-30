package com.banm.abb.StocksApp.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class StocksOwnedDto {

    private String name;
    private int count;
    private BigDecimal totalValue;
}
