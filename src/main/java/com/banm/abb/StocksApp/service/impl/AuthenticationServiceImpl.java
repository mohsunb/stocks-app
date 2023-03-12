package com.banm.abb.StocksApp.service.impl;

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
import com.banm.abb.StocksApp.service.AuthenticationService;
import com.banm.abb.StocksApp.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UsersRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    private final UsersRepository usersRepository;
    private final EmailConfirmationTokensRepository tokensRepository;
    private final EmailService emailService;

    public String register(RegisterRequestDto request) {

        var user_temp = usersRepository.findByUsername(request.getEmail());

        if (user_temp.isPresent())
            throw new EmailAlreadyInUseException("Invalid request. This email is already in use.");

        var user = User.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .username(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .balance(BigDecimal.ZERO)
                .enabled(false)
                .build();
        repository.save(user);

        EmailConfirmationToken token = new EmailConfirmationToken(request.getEmail());
        tokensRepository.save(token);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(request.getEmail());
        message.setSubject("Email verification");
        message.setFrom("stocksappsmtpmohsun@gmail.com");
        message.setText("To confirm your account, please click this link: " +
                "http://localhost:8080/api/v1/auth/confirm-account?token=" + token.getToken());

        emailService.sendMail(message);

        return "Registry successful. A confirmation link was sent to \"" + request.getEmail() + "\".";
    }

    public AuthenticationResponseDto authenticate(AuthenticationRequestDto request) {
        var user = repository.findByUsername(request.getEmail()).orElseThrow(() -> new UserDoesNotExistException("Such user does not exist."));
        authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var jwt = jwtService.generateToken(user);

        return AuthenticationResponseDto.builder().token(jwt).build();
    }

    public String confirmAccount(String token) {
        var confirmationToken = tokensRepository.findEmailConfirmationTokenByToken(token);

        if (confirmationToken.isPresent()) {
            User user = usersRepository.findByUsername(confirmationToken.orElseThrow().getUsername()).orElseThrow();
            user.setEnabled(true);
            usersRepository.save(user);
            return "Account successfully confirmed.";
        }

        throw new InvalidConfirmationTokenException("The confirmation link is invalid.");
    }
}
