package com.tma.english.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;
public class EmailValidator implements ConstraintValidator<Email, String> {
    private String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        if (email == null || email.isEmpty())
            return false;
        Pattern pat = Pattern.compile(regex);
        return pat.matcher(email).matches();

    }
}
