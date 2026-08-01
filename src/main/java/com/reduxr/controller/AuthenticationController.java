package com.reduxr.controller;

import com.reduxr.dto.user.UserRegistrationRequestDto;
import com.reduxr.dto.user.UserResponseDto;
import com.reduxr.exception.RegistrationException;
import com.reduxr.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/book-store/auth")
public class AuthenticationController {
    private final UserService userService;
    
    @PostMapping
    public UserResponseDto register(@RequestBody @Valid UserRegistrationRequestDto request) 
            throws RegistrationException {
        return userService.registerUser(request);
    }
}
