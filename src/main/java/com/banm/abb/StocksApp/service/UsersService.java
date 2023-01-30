package com.banm.abb.StocksApp.service;

import com.banm.abb.StocksApp.dto.CurrentUserInfoDto;
import com.banm.abb.StocksApp.dto.DepositRequestDto;
import com.banm.abb.StocksApp.entity.User;
import com.banm.abb.StocksApp.repository.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsersService {

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
        Long userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        User user = usersRepository.findById(userId).orElseThrow();
        user.setBalance(user.getBalance().add(request.getAmount()));
        return "Operation successful. $" + request.getAmount() + " was deposited. Balance: $" + user.getBalance() + ".";
    }
}
