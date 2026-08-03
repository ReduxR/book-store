package com.reduxr.dto.user;

import com.reduxr.validator.matcher.FieldMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@FieldMatch(
        first = "password", 
        second = "repeatPassword", 
        message = "do not match with first value")
public class UserRegistrationRequestDto {
    @NotBlank
    @Email
    private String email;
    
    @NotBlank
    @Size(min = 4)
    private String password;
    
    @NotBlank
    @Size(min = 4)
    private String repeatPassword;
    
    @NotBlank
    private String firstName;
    
    @NotBlank
    private String lastName;
    
    private String shippingAddress;
}
