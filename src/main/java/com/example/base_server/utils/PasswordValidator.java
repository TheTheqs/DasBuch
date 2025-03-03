package com.example.base_server.utils;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;
@Component
public class PasswordValidator {
    //Standards
    private static final int MIN_LENGTH = 8;
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()-+";
    private static final Pattern UPPERCASE = Pattern.compile("[A-Z]");
    private static final Pattern LOWERCASE = Pattern.compile("[a-z]");
    private static final Pattern DIGIT = Pattern.compile("[0-9]");
    private static final Pattern SPECIAL = Pattern.compile("[" + SPECIAL_CHARACTERS + "]");

    //Validation
    public static String isValid(String password) {
        if (password == null || password.length() < MIN_LENGTH) return "Password must be at least " + MIN_LENGTH + " characters long.";
        if (!UPPERCASE.matcher(password).find()) return "Password must contain at least one uppercase letter.";
        if (!LOWERCASE.matcher(password).find()) return "Password must contain at least one lowercase letter.";
        if (!DIGIT.matcher(password).find()) return "Password must contain at least one digit.";
        if (!SPECIAL.matcher(password).find()) return "Password must contain at least one special character: " + SPECIAL_CHARACTERS;
        return null;
    }
}
