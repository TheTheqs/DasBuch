package com.example.base_server.utils;

import java.util.regex.Pattern;
//This class will check the email format and validate it.
public class EmailValidator {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final Pattern pattern = Pattern.compile(EMAIL_REGEX);

    public static boolean isValidEmail(String email) {
        return email != null && pattern.matcher(email).matches();
    }
}
