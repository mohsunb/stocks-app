package com.banm.abb.StocksApp.service;

import com.banm.abb.StocksApp.dto.CurrentUserInfoDto;
import com.banm.abb.StocksApp.dto.DepositRequestDto;

public interface UsersService {

    CurrentUserInfoDto getCurrentUserInfo();

    String depositMoney(DepositRequestDto request);
}
