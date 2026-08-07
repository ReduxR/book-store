package com.reduxr.service.impl;

import com.reduxr.dto.user.UserLoginRequestDto;
import com.reduxr.dto.user.UserLoginResponseDto;
import com.reduxr.security.jwt.JwtUtil;
import com.reduxr.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    
    @Override
    public UserLoginResponseDto authenticateUser(UserLoginRequestDto requestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDto.getEmail(), requestDto.getPassword())
        );
        
        String generatedToken = jwtUtil.generateToken(authentication.getName());
        return new UserLoginResponseDto(generatedToken);
    }
}
