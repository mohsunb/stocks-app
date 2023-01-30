package com.banm.abb.StocksApp.service;

import com.banm.abb.StocksApp.dto.GetUsersDto;
import com.banm.abb.StocksApp.dto.RegisterRequestDto;
import com.banm.abb.StocksApp.entity.User;
import com.banm.abb.StocksApp.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;

    public void register(RegisterRequestDto dto) {

    }

    public List<GetUsersDto> getUsers() {
        List<GetUsersDto> list = new ArrayList<>();

        for (User u : usersRepository.findAll())
            list.add(GetUsersDto.builder()
                    .name(u.getName())
                    .surname(u.getSurname())
                    .email(u.getUsername())
                    .build());

        return list;
    }
}
