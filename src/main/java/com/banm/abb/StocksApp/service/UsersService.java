package com.banm.abb.StocksApp.service;

import com.banm.abb.StocksApp.dto.CurrentUserInfoDto;
import com.banm.abb.StocksApp.dto.DepositRequestDto;
import com.banm.abb.StocksApp.exception.InvalidDepositRequestException;
import com.banm.abb.StocksApp.model.User;
import com.banm.abb.StocksApp.repository.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

public interface UsersService {

    CurrentUserInfoDto getCurrentUserInfo();

    String depositMoney(DepositRequestDto request);
}
