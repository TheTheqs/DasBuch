package com.example.base_server.utils;

public class PasswordValidator {
    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

    public static boolean isValid(String password) {
        return password != null && password.matches(PASSWORD_REGEX);
    }
}
