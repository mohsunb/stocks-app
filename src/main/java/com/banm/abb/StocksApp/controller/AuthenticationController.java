package com.banm.abb.StocksApp.controller;

import com.banm.abb.StocksApp.dto.AuthenticationRequestDto;
import com.banm.abb.StocksApp.dto.AuthenticationResponseDto;
import com.banm.abb.StocksApp.dto.RegisterRequestDto;
import com.banm.abb.StocksApp.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequestDto request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @GetMapping("/confirm-account")
    public ResponseEntity<String> confirmAccount(@RequestParam("token") String token) {
        return ResponseEntity.ok(authService.confirmAccount(token));
    }

    @PostMapping("/auth")
    public ResponseEntity<AuthenticationResponseDto> authenticate(@RequestBody AuthenticationRequestDto request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
}
