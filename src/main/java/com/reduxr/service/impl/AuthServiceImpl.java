package com.reduxr.service.impl;

import com.reduxr.dto.user.UserRegistrationRequestDto;
import com.reduxr.dto.user.UserResponseDto;
import com.reduxr.mapper.UserMapper;
import com.reduxr.model.User;
import com.reduxr.repository.UserRepository;
import com.reduxr.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository repository;
    private final UserMapper mapper;
    
    @Override
    public UserResponseDto registerUser(UserRegistrationRequestDto registrationRequestDto) {
        User saved = repository.save(mapper.toModel(registrationRequestDto));
        return mapper.toDto(saved);
    }
}
