package com.banm.abb.StocksApp.controller;

import com.banm.abb.StocksApp.dto.AuthenticationRequestDto;
import com.banm.abb.StocksApp.dto.AuthenticationResponseDto;
import com.banm.abb.StocksApp.dto.RegisterRequestDto;
import com.banm.abb.StocksApp.entity.EmailConfirmationToken;
import com.banm.abb.StocksApp.entity.User;
import com.banm.abb.StocksApp.repository.EmailConfirmationTokensRepository;
import com.banm.abb.StocksApp.repository.UsersRepository;
import com.banm.abb.StocksApp.service.AuthenticationService;
import com.banm.abb.StocksApp.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authService;
    private final UsersRepository usersRepository;
    private final EmailConfirmationTokensRepository tokensRepository;
    private final EmailService emailService;

    @GetMapping("/register")
    public ModelAndView displayRegisterForm(ModelAndView modelAndView, RegisterRequestDto request) {
        modelAndView.addObject(request);
        modelAndView.setViewName("register");
        return modelAndView;
    }

    @PostMapping("/register")
    public ModelAndView register(ModelAndView modelAndView, RegisterRequestDto request) {
        var user = usersRepository.findByUsername(request.getEmail());

        if (user.isPresent()) {
            modelAndView.addObject("message", "This email already exists!");
            modelAndView.setViewName("error");
        }

        else {
            authService.register(request);
            EmailConfirmationToken token = new EmailConfirmationToken(request.getEmail());
            tokensRepository.save(token);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(request.getEmail());
            message.setSubject("Email verification");
            message.setFrom("");
            message.setText("To confirm you account, please click this link: " +
                    "http://localhost:8080/api/v1/auth/confirm-account?token=" + token.getToken());

            emailService.sendMail(message);

            modelAndView.addObject("email", request.getEmail());
            modelAndView.setViewName("successfulRegistration");
        }

        return modelAndView;
    }

    @RequestMapping(value = "/confirm-account", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView confirmAccount(ModelAndView modelAndView, @RequestParam("token") String token) {
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

    @PostMapping("/auth")
    public ResponseEntity<AuthenticationResponseDto> authenticate(@RequestBody AuthenticationRequestDto request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
}
