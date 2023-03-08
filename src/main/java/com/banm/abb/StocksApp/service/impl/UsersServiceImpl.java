package com.banm.abb.StocksApp.service.impl;

import com.banm.abb.StocksApp.dto.CurrentUserInfoDto;
import com.banm.abb.StocksApp.dto.DepositRequestDto;
import com.banm.abb.StocksApp.exception.InvalidDepositRequestException;
import com.banm.abb.StocksApp.model.User;
import com.banm.abb.StocksApp.repository.UsersRepository;
import com.banm.abb.StocksApp.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;

    public CurrentUserInfoDto getCurrentUserInfo() {
        var user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return CurrentUserInfoDto.builder()
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getUsername())
                .balance(user.getBalance()).build();
    }

    public String depositMoney(DepositRequestDto request) {
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0)
            throw new InvalidDepositRequestException("Deposited amount must be greater than zero.");

        var user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setBalance(user.getBalance().add(request.getAmount()));
        usersRepository.save(user);
        return "Operation successful. $" + request.getAmount() + " was deposited. Balance: $" + user.getBalance() + ".";
    }
}
