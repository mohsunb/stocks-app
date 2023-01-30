package com.banm.abb.StocksApp.controller;

import com.banm.abb.StocksApp.dto.CurrentUserInfoDto;
import com.banm.abb.StocksApp.dto.DepositRequestDto;
import com.banm.abb.StocksApp.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;

    @GetMapping("/current")
    public ResponseEntity<CurrentUserInfoDto> getCurrentUser() {
        return ResponseEntity.ok(usersService.getCurrentUserInfo());
    }

    @PostMapping("/current/deposit")
    public ResponseEntity<String> depositMoney(@RequestBody DepositRequestDto request) {
        return ResponseEntity.ok(usersService.depositMoney(request));
    }
}
