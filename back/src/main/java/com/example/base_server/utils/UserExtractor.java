package com.example.base_server.utils;

import com.example.base_server.config.CustomUserDetails;
import com.example.base_server.model.User;
import org.springframework.security.core.Authentication;

public class UserExtractor {

    public static User extractUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User not authenticated");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomUserDetails(User user)) {
            return user;
        }

        throw new IllegalStateException("Unexpected principal type: " + principal.getClass());
    }
}
