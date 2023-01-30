package com.banm.abb.StocksApp.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CurrentUserInfoDto {
    private String name;
    private String surname;
    private String email;
    private BigDecimal balance;
}
