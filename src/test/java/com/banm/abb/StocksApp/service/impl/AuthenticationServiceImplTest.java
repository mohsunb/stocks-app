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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {
    @InjectMocks
    private AuthenticationServiceImpl service;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private EmailConfirmationTokensRepository tokensRepository;

    @Mock
    @SuppressWarnings("unused")
    private EmailServiceImpl emailService;

    @Mock
    @SuppressWarnings("unused")
    private PasswordEncoder encoder;

    @Mock
    private JwtService jwtService;

    @Mock
    @SuppressWarnings("unused")
    private AuthenticationManager authenticationManager;

    @Test
    void registerWhenUserPresentThrow() {
        User user = User.builder().build();
        when(usersRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        RegisterRequestDto dto = RegisterRequestDto.builder().email("").build();
        assertThrows(EmailAlreadyInUseException.class, () -> service.register(dto));
    }

    @Test
    void registerThenSuccess() {
        var dto = RegisterRequestDto.builder()
                .name("Mohsun")
                .surname("Babayev")
                .email("example@gmail.com")
                .password("password")
                .build();

        assertEquals("Registry successful. A confirmation link was sent to \""
                + dto.getEmail() + "\".", service.register(dto));
    }

    @Test
    void authenticateWhenUserInvalidThenThrow() {
        when(usersRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        var dto = AuthenticationRequestDto.builder().email("").build();
        assertThrows(UserDoesNotExistException.class, () -> service.authenticate(dto));
    }

    @Test
    void authenticateSuccess() {
        var request = AuthenticationRequestDto.builder().email("").build();
        var user = User.builder().build();
        when(usersRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        String jwt = "jwt";
        when(jwtService.generateToken(any())).thenReturn(jwt);
        var response = AuthenticationResponseDto.builder().token(jwt).build();
        assertEquals(service.authenticate(request), response);
    }

    @Test
    void confirmAccountThrowWhenInvalid() {
        when(tokensRepository.findEmailConfirmationTokenByToken(anyString())).thenReturn(Optional.empty());
        assertThrows(InvalidConfirmationTokenException.class, () -> service.confirmAccount(""));
    }

    @Test
    void confirmAccountSuccess() {
        var confirmationToken = new EmailConfirmationToken("test_username");
        when(tokensRepository.findEmailConfirmationTokenByToken(anyString())).thenReturn(Optional.of(confirmationToken));

        User user = User.builder()
                .name("Mohsun")
                .surname("Babayev")
                .username("example@mail.com")
                .password("123456789")
                .role(Role.USER)
                .balance(BigDecimal.ZERO)
                .enabled(false)
                .build();
        when(usersRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        assertEquals(service.confirmAccount(""), "Account successfully confirmed.");
    }
}
