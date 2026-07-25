package com.reduxr.validator.isbn;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class IsbnValidator implements ConstraintValidator<Isbn, String> {
    private static final String ISBN_REGEX = 
            "^(?=(?:[^0-9]*[0-9]){10}(?:(?:[^0-9]*[0-9]){3})?$)[\\d-]+$";
    
    @Override
    public boolean isValid(String isbn, ConstraintValidatorContext context) {
        return isbn != null && Pattern.compile(ISBN_REGEX).matcher(isbn).matches();
    }
}
