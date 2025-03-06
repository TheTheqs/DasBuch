package com.example.base_server.utils;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class NameValidator {
    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 50;
    private static final Pattern VALID_NAME_PATTERN = Pattern.compile("^[A-Za-zÀ-ÖØ-öø-ÿ ]+$");

    public static String isValid(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "Name cannot be empty.";
        }
        if (name.length() < MIN_LENGTH) {
            return "Name must be at least " + MIN_LENGTH + " characters long.";
        }
        if (name.length() > MAX_LENGTH) {
            return "Name must be at most " + MAX_LENGTH + " characters long.";
        }
        if (!VALID_NAME_PATTERN.matcher(name).matches()) {
            return "Name can only contain letters and spaces.";
        }
        return null; // A null return indicates a valid name
    }
}
