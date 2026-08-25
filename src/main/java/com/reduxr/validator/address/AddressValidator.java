package com.reduxr.validator.address;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class AddressValidator implements ConstraintValidator<Address, String> {
    public static final String ADDRESS_REGEX = 
            "^([A-Za-z][A-Za-z '-]*),\\s+([A-Za-z][A-Za-z '-]*),\\s+(\\d+[A-Za-z]?)$";
    
    @Override
    public boolean isValid(String address, ConstraintValidatorContext context) {
        return address != null && Pattern.compile(ADDRESS_REGEX).matcher(address).matches();
    }
}
