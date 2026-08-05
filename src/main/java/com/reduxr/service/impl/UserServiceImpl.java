package com.reduxr.service.impl;

import com.reduxr.dto.user.UserRegistrationRequestDto;
import com.reduxr.dto.user.UserResponseDto;
import com.reduxr.exception.RegistrationException;
import com.reduxr.mapper.UserMapper;
import com.reduxr.model.Role;
import com.reduxr.model.User;
import com.reduxr.repository.RoleRepository;
import com.reduxr.repository.UserRepository;
import com.reduxr.security.RoleName;
import com.reduxr.service.UserService;
import jakarta.transaction.Transactional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;
    
    @Transactional
    @Override
    public UserResponseDto registerUser(UserRegistrationRequestDto registrationRequestDto) {
        if (userRepository.existsByEmail(registrationRequestDto.getEmail())) {
            throw new RegistrationException(String.format("User with email: %s already exists", 
                    registrationRequestDto.getEmail()));
        }
        
        Role role = roleRepository.findByName(RoleName.USER)
                .orElseThrow(() ->
                        new RegistrationException("default USER role is missing"));
        
        User user = mapper.toModel(registrationRequestDto);
        user.setRoles(Set.of(role));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        User savedUser = userRepository.save(user);
        return mapper.toDto(savedUser);
    }
}
