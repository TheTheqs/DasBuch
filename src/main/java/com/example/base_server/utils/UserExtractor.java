package com.example.base_server.utils;

import com.example.base_server.model.User;
import com.example.base_server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class UserExtractor {
    private final UserService userService;

    @Autowired
    public UserExtractor(UserService userService) {
        this.userService = userService;
    }

    // Extract a user from an authentication
    public User getUserFromAuth(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails userDetails) {
            return userService.findByEmail(userDetails.getUsername()).orElse(null);
        }
        return null;
    }
}
