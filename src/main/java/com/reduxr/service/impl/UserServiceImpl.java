package com.reduxr.service.impl;

import com.reduxr.dto.user.UserRegistrationRequestDto;
import com.reduxr.dto.user.UserResponseDto;
import com.reduxr.exception.RegistrationException;
import com.reduxr.mapper.UserMapper;
import com.reduxr.model.User;
import com.reduxr.repository.UserRepository;
import com.reduxr.service.UserService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;
    
    @Override
    public UserResponseDto registerUser(UserRegistrationRequestDto registrationRequestDto) {
        if (!userExists(registrationRequestDto.getEmail())) {
            User saved = repository.save(mapper.toModel(registrationRequestDto));
            return mapper.toDto(saved);
        }
        throw new RegistrationException("User with given email already exists");
    }
    
    private boolean userExists(String email) {
        Optional<User> userByEmail = repository.findByEmail(email);
        return userByEmail.isPresent();
    }
}
