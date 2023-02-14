package com.banm.abb.StocksApp.service;

import com.banm.abb.StocksApp.dto.AuthenticationRequestDto;
import com.banm.abb.StocksApp.dto.AuthenticationResponseDto;
import com.banm.abb.StocksApp.dto.RegisterRequestDto;
import com.banm.abb.StocksApp.exception.EmailAlreadyInUseException;
import com.banm.abb.StocksApp.exception.InvalidConfirmationTokenException;
import com.banm.abb.StocksApp.exception.UserDoesNotExistException;
import com.banm.abb.StocksApp.model.EmailConfirmationToken;
import com.banm.abb.StocksApp.model.Role;
import com.banm.abb.StocksApp.model.User;
import com.banm.abb.StocksApp.repository.EmailConfirmationTokensRepository;
import com.banm.abb.StocksApp.repository.UsersRepository;
import com.banm.abb.StocksApp.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

public interface AuthenticationService {

    String register(RegisterRequestDto request);

    AuthenticationResponseDto authenticate(AuthenticationRequestDto request);

    String confirmAccount(String token);
}
