package com.banm.abb.StocksApp.service.impl;

import com.banm.abb.StocksApp.dto.CurrentUserInfoDto;
import com.banm.abb.StocksApp.dto.DepositRequestDto;
import com.banm.abb.StocksApp.exception.InvalidDepositRequestException;
import com.banm.abb.StocksApp.model.User;
import com.banm.abb.StocksApp.repository.UsersRepository;
import com.banm.abb.StocksApp.service.UsersService;
import jakarta.transaction.Transactional;
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

    @Transactional
    public String depositMoney(DepositRequestDto request) {
        if (request.getAmount().compareTo(new BigDecimal("0")) <= 0)
            throw new InvalidDepositRequestException("Deposited amount must be greater than zero.");

        Long userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        User user = usersRepository.findById(userId).orElseThrow();
        user.setBalance(user.getBalance().add(request.getAmount()));
        return "Operation successful. $" + request.getAmount() + " was deposited. Balance: $" + user.getBalance() + ".";
    }
}
