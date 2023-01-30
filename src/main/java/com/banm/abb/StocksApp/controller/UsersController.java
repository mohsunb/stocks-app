package com.banm.abb.StocksApp.controller;

import com.banm.abb.StocksApp.dto.GetUsersDto;
import com.banm.abb.StocksApp.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users-list")
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;

    @GetMapping
    public ResponseEntity<List<GetUsersDto>> getUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(usersService.getUsers());
    }
}
