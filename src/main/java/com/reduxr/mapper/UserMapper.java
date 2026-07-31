package com.reduxr.mapper;

import com.reduxr.config.MapperConfig;
import com.reduxr.dto.user.UserRegistrationRequestDto;
import com.reduxr.dto.user.UserResponseDto;
import com.reduxr.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toDto(User user);
    
    User toModel(UserRegistrationRequestDto registrationRequestDto);
}
