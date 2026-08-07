package com.reduxr.service;

import com.reduxr.dto.user.UserLoginRequestDto;
import com.reduxr.dto.user.UserLoginResponseDto;

public interface AuthenticationService {
    UserLoginResponseDto authenticateUser(UserLoginRequestDto requestDto);
}
