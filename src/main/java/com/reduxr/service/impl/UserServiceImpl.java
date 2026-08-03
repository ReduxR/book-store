package com.reduxr.service.impl;

import com.reduxr.dto.user.UserRegistrationRequestDto;
import com.reduxr.dto.user.UserResponseDto;
import com.reduxr.exception.RegistrationException;
import com.reduxr.mapper.UserMapper;
import com.reduxr.model.User;
import com.reduxr.repository.UserRepository;
import com.reduxr.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;
    
    @Override
    public UserResponseDto registerUser(UserRegistrationRequestDto registrationRequestDto) {
        if (repository.existsByEmail(registrationRequestDto.getEmail())) {
            throw new RegistrationException(String.format("User with email: %s already exists", 
                    registrationRequestDto.getEmail()));
        }
        User user = repository.save(mapper.toModel(registrationRequestDto));
        return mapper.toDto(user);
    }
}
