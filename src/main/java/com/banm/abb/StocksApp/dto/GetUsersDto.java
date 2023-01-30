package com.banm.abb.StocksApp.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GetUsersDto {

    private String name;
    private String surname;
    private String email;
}
