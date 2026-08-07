package com.reduxr.controller;

import com.reduxr.dto.user.UserLoginRequestDto;
import com.reduxr.dto.user.UserLoginResponseDto;
import com.reduxr.dto.user.UserRegistrationRequestDto;
import com.reduxr.dto.user.UserResponseDto;
import com.reduxr.exception.RegistrationException;
import com.reduxr.service.AuthenticationService;
import com.reduxr.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication", description = "Endpoints for login and registration")
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;
    
    @Operation(summary = "Registration", description = "Endpoint for registering a new user")
    @PostMapping("/registration")
    public UserResponseDto register(@RequestBody @Valid UserRegistrationRequestDto request) 
            throws RegistrationException {
        return userService.registerUser(request);
    }
    
    @GetMapping("/login")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto request) {
        return authenticationService.authenticateUser(request);
    }
}
