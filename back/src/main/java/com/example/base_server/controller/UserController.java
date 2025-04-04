package com.example.base_server.controller;

import com.example.base_server.dto.UserDTO;
import com.example.base_server.enums.Role;
import com.example.base_server.service.UserService;
import com.example.base_server.utils.UserExtractor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 1 - Register
    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestParam String name,
                                                @RequestParam String email,
                                                @RequestParam String password) {
        var newUser = new UserDTO(userService.registerUser(name, email, password, Role.USER));
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    // 2 - Verify
    @GetMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestParam String token) {
        return userService.verifyUser(token)
                ? ResponseEntity.ok("Account verified successfully!")
                : ResponseEntity.badRequest().body("Invalid or expired verification token.");
    }

    // 3 - Login
    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(HttpServletRequest request,
                                         @RequestParam String email,
                                         @RequestParam String password) {
        var user = new UserDTO(userService.login(email, password));
        request.getSession().setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
        return ResponseEntity.ok(user);
    }

    // 4 - Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully.");
    }

    // 5 - Auth Check
    @GetMapping("/me")
    public ResponseEntity<String> isValid(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authenticated.");
        }
        UserDTO user =  new UserDTO(UserExtractor.extractUser(authentication));
        return ResponseEntity.ok("You are validated as: " + user);
    }

    // 6 - Logout
    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(
                request,
                response,
                SecurityContextHolder.getContext().getAuthentication()
        );
        return ResponseEntity.ok("You have been logged out successfully!");
    }
}
