package com.reduxr.validator.matcher;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;
import org.springframework.beans.BeanWrapperImpl;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    private String firstFieldName;
    private String secondFieldName;
    
    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        this.firstFieldName = constraintAnnotation.first();
        this.secondFieldName = constraintAnnotation.second();
    }
    
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Object first = new BeanWrapperImpl(value).getPropertyValue(firstFieldName);
        Object second = new BeanWrapperImpl(value).getPropertyValue(secondFieldName);
        
        boolean isValid = Objects.equals(first, second);
        
        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode(secondFieldName)
                    .addConstraintViolation();
        }
        return isValid;
    }
}
