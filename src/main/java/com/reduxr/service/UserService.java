package com.reduxr.service;

import com.reduxr.dto.user.UserRegistrationRequestDto;
import com.reduxr.dto.user.UserResponseDto;

public interface UserService {
    UserResponseDto registerUser(UserRegistrationRequestDto registrationRequestDto);
}
