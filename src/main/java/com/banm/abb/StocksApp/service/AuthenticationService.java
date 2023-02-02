package com.banm.abb.StocksApp.service;

import com.banm.abb.StocksApp.dto.AuthenticationRequestDto;
import com.banm.abb.StocksApp.dto.AuthenticationResponseDto;
import com.banm.abb.StocksApp.dto.RegisterRequestDto;
import com.banm.abb.StocksApp.entity.EmailConfirmationToken;
import com.banm.abb.StocksApp.entity.Role;
import com.banm.abb.StocksApp.entity.User;
import com.banm.abb.StocksApp.repository.EmailConfirmationTokensRepository;
import com.banm.abb.StocksApp.repository.UsersRepository;
import com.banm.abb.StocksApp.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UsersRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    private final UsersRepository usersRepository;
    private final EmailConfirmationTokensRepository tokensRepository;
    private final EmailService emailService;

    public ModelAndView displayRegistryForm(ModelAndView modelAndView, RegisterRequestDto request) {
        modelAndView.addObject(request);
        modelAndView.setViewName("register");
        return modelAndView;
    }

    public ModelAndView register(ModelAndView modelAndView, RegisterRequestDto request) {

        var user_temp = usersRepository.findByUsername(request.getEmail());

        if (user_temp.isPresent()) {
            modelAndView.addObject("message", "This email already exists!");
            modelAndView.setViewName("error");
        }

        else {
            var user1 = User.builder()
                    .name(request.getName())
                    .surname(request.getSurname())
                    .username(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.USER)
                    .balance(new BigDecimal("0"))
                    .enabled(false)
                    .build();
            repository.save(user1);

            EmailConfirmationToken token = new EmailConfirmationToken(request.getEmail());
            tokensRepository.save(token);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(request.getEmail());
            message.setSubject("Email verification");
            message.setFrom("stocksappsmtpmohsun@gmail.com");
            message.setText("To confirm you account, please click this link: " +
                    "http://localhost:8080/api/v1/auth/confirm-account?token=" + token.getToken());

            emailService.sendMail(message);

            modelAndView.addObject("email", request.getEmail());
            modelAndView.setViewName("successfulRegistration");
        }

        return modelAndView;
    }

    public AuthenticationResponseDto authenticate(AuthenticationRequestDto request) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        var user = repository.findByUsername(request.getEmail()).orElseThrow();
        var jwt = jwtService.generateToken(user);

        return AuthenticationResponseDto.builder().token(jwt).build();
    }

    public ModelAndView confirmAccount(ModelAndView modelAndView, String token) {
        var confirmationToken = tokensRepository.findEmailConfirmationTokenByToken(token);

        if (confirmationToken.isPresent()) {
            User user = usersRepository.findByUsername(confirmationToken.orElseThrow().getUsername()).orElseThrow();
            user.setEnabled(true);
            usersRepository.save(user);
            modelAndView.setViewName("accountVerified");
        }

        else {
            modelAndView.addObject("message", "The link is invalid or broken!");
            modelAndView.setViewName("error");
        }

        return modelAndView;
    }
}
