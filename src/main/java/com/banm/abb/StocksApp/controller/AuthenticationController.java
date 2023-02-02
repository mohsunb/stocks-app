package com.banm.abb.StocksApp.controller;

import com.banm.abb.StocksApp.dto.AuthenticationRequestDto;
import com.banm.abb.StocksApp.dto.AuthenticationResponseDto;
import com.banm.abb.StocksApp.dto.RegisterRequestDto;
import com.banm.abb.StocksApp.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authService;

    @GetMapping("/register")
    public ModelAndView displayRegisterForm(ModelAndView modelAndView, RegisterRequestDto request) {
        return authService.displayRegistryForm(modelAndView, request);
    }

    @PostMapping("/register")
    public ModelAndView register(ModelAndView modelAndView, RegisterRequestDto request) {
        return authService.register(modelAndView, request);
    }

    @RequestMapping(value = "/confirm-account", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView confirmAccount(ModelAndView modelAndView, @RequestParam("token") String token) {
        return authService.confirmAccount(modelAndView, token);
    }

    @PostMapping("/auth")
    public ResponseEntity<AuthenticationResponseDto> authenticate(@RequestBody AuthenticationRequestDto request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
}
