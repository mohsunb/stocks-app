package com.banm.abb.StocksApp.service.impl;

import com.banm.abb.StocksApp.dto.CurrentUserInfoDto;
import com.banm.abb.StocksApp.dto.DepositRequestDto;
import com.banm.abb.StocksApp.exception.InvalidDepositRequestException;
import com.banm.abb.StocksApp.model.Role;
import com.banm.abb.StocksApp.model.User;
import com.banm.abb.StocksApp.repository.UsersRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsersServiceImplTest {

    @InjectMocks
    private UsersServiceImpl service;

    @Mock
    private SecurityContext context;

    @Mock
    private Authentication auth;

    @Mock
    private UsersRepository usersRepository;

    @Test
    void getCurrentUserInfoTest() {
        User user = User.builder()
                .id(0L)
                .name("Mohsun")
                .surname("Babayev")
                .username("mohsun.babayev.2003@gmail.com")
                .password("12345678")
                .role(Role.ADMIN)
                .balance(BigDecimal.ZERO)
                .enabled(true)
                .build();

        when(auth.getPrincipal()).thenReturn(user);
        when(context.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(context);

        var dto = CurrentUserInfoDto.builder()
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getUsername())
                .balance(user.getBalance())
                .build();

        assertEquals(dto, service.getCurrentUserInfo());
    }

    @Test
    void depositMoneyThrowWhenZeroAmount() {
        var e = assertThrows(InvalidDepositRequestException.class, () -> {
            service.depositMoney(DepositRequestDto.builder().amount(BigDecimal.ZERO).build());
        });

        assertEquals(e.getMessage(), "Deposited amount must be greater than zero.");
    }

    @Test
    void depositMoneyThrowWhenNegativeAmount() {
        var e = assertThrows(InvalidDepositRequestException.class, () -> {
            service.depositMoney(DepositRequestDto.builder().amount(new BigDecimal("-1")).build());
        });

        assertEquals(e.getMessage(), "Deposited amount must be greater than zero.");
    }

    @Test
    void depositMoneySuccessWhenValidAmount() {
        User user = User.builder()
                .id(0L)
                .name("Mohsun")
                .surname("Babayev")
                .username("mohsun.babayev.2003@gmail.com")
                .password("12345678")
                .role(Role.ADMIN)
                .balance(BigDecimal.ONE)
                .enabled(true)
                .build();

        when(auth.getPrincipal()).thenReturn(user);
        when(context.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(context);

        DepositRequestDto request = DepositRequestDto.builder()
                .amount(BigDecimal.TEN)
                .build();

        when(usersRepository.save(any())).thenReturn(user);
        assertEquals(service.depositMoney(request),
                "Operation successful. $" + request.getAmount() +
                        " was deposited. Balance: $" + user.getBalance() + ".");
    }
}
