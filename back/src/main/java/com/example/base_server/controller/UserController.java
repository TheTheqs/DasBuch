package com.example.base_server.controller;

import com.example.base_server.dto.CreateUserDTO;
import com.example.base_server.dto.UserDTO;
import com.example.base_server.enums.Role;
import com.example.base_server.service.UserService;
import com.example.base_server.utils.UserExtractor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<UserDTO> registerUser(@RequestBody CreateUserDTO dto) {
        var newUser = new UserDTO(userService.registerUser(dto.getName(), dto.getEmail(), dto.getPassword(), Role.USER));
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    // 2 - Verify
    @GetMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestParam String token) {
        boolean verified = userService.verifyUser(token);
        return verified
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
        UserDTO user = new UserDTO(UserExtractor.extractUser(authentication));
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

    // 7 - Change user attributes
    @PatchMapping("/update")
    public ResponseEntity<UserDTO> updateUser(Authentication authentication,
                                              @RequestParam String name,
                                              @RequestParam String password) {
        var updatedUser = new UserDTO(
                userService.updateUser(UserExtractor.extractUser(authentication),
                        name,
                        password));

        return ResponseEntity.ok(updatedUser);
    }

    //8- Request reset password
    @PostMapping("/forgot-password")
    public ResponseEntity<String> requestPasswordReset(@RequestParam String email) {
        userService.generatePasswordToken(email);
        return ResponseEntity.ok("If this email exists, a password reset link has been sent.");
    }

    //9- Reset password
    @PatchMapping("/reset")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String password) {
        String message = userService.resetPassword(token, password) ?
                "Password reset successfully." :
                "Password reset failed. Please try again.";

        return ResponseEntity.ok(message);
    }

    //10- Search user by name
    @GetMapping("/search")
    public ResponseEntity<Page<UserDTO>> searchUsername(@RequestParam String name, Pageable pageable) {

        Page<UserDTO> result = userService.searchUserByName(name, pageable).map(UserDTO::new);

        return ResponseEntity.ok(result);
    }

    //11 Get users by read book
    @GetMapping("/book")
    public ResponseEntity<Page<UserDTO>> searchUserByBook(@RequestParam Long id, Pageable pageable) {

        Page<UserDTO> result = userService.searchByReadBook(id, pageable).map(UserDTO::new);

        return ResponseEntity.ok(result);
    }
}
