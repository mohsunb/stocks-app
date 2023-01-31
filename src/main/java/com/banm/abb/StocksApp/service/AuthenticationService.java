package com.banm.abb.StocksApp.service;

import com.banm.abb.StocksApp.dto.AuthenticationRequestDto;
import com.banm.abb.StocksApp.dto.AuthenticationResponseDto;
import com.banm.abb.StocksApp.dto.RegisterRequestDto;
import com.banm.abb.StocksApp.entity.Role;
import com.banm.abb.StocksApp.entity.User;
import com.banm.abb.StocksApp.repository.UsersRepository;
import com.banm.abb.StocksApp.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UsersRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    public AuthenticationResponseDto register(RegisterRequestDto request) {
        var user = User.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .username(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .balance(new BigDecimal("0"))
                .build();
        repository.save(user);

        var jwt = jwtService.generateToken(user);
        return AuthenticationResponseDto.builder().token(jwt).build();
    }

    public AuthenticationResponseDto authenticate(AuthenticationRequestDto request) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        var user = repository.findByUsername(request.getEmail()).orElseThrow();
        var jwt = jwtService.generateToken(user);

        return AuthenticationResponseDto.builder().token(jwt).build();
    }
}
