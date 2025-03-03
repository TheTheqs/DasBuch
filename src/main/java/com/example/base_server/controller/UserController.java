package com.example.base_server.controller;

import com.example.base_server.dto.UserDTO;
import com.example.base_server.enums.Role;
import com.example.base_server.model.User;
import com.example.base_server.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController //Type declaration
@RequestMapping("/users") //Base EndPoint address
public class UserController {

    @Autowired //Dependency configuration
    private UserService userService;

    //1- POST: Register a New User
    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestParam String name,
                                                @RequestParam String email,
                                                @RequestParam String password) {
        UserDTO newUser = new UserDTO(userService.registerUser(name, email, password, Role.USER)); //Role.USER

        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    //2- Verify user account via email token
    @GetMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestParam String token) {
        boolean isVerified = userService.verifyUser(token);
        return isVerified ? ResponseEntity.ok("Account verified successfully!")
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired verification token.");
    }

    //3- Login (returns user details without password)
    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(HttpServletRequest request, @RequestParam String email, @RequestParam String password) {
        UserDTO user = new UserDTO(userService.login(email, password));

        //This makes de session persistent
        request.getSession().setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

        return ResponseEntity.ok(user);
    }

    //4- Delete user by ID.
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully.");
    }

    //5- Get logged User
    @GetMapping("/me")
    public ResponseEntity<?> getLoggedUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authenticated.");
        }
        //Retrieve the currently authenticated user from the security context.
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails userDetails) { //Extracts UserDetails from Authentication
            //Calls the service layer to retrieve the full User entity based on the email (used as username)
            Optional<User> optionalUser = userService.findByEmail(userDetails.getUsername());
            if(optionalUser.isPresent()){
               return ResponseEntity.ok(new UserDTO(optionalUser.get()));
            }
        }
        //If user not found, but authenticated (Which is an error) returns the authentication name.
        return ResponseEntity.ok("You are now validated as: " + authentication.getName());
    }

    //6- Logout
    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return ResponseEntity.ok("You have been logged out successfully!");
    }

    //7- Request password reset
    @PostMapping("/request-reset")
    public ResponseEntity<String> requestReset(@RequestParam String email) {
        userService.requestReset(email);
        return ResponseEntity.ok("If an account with this email exists, you will receive a password reset link shortly. Please check your inbox and spam folder.");
    }

    //8- Reset password
    @PutMapping("/reset")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String password) {
        boolean canReset = userService.resetPassword(token, password);
        return canReset ? ResponseEntity.ok("Password has been reset successfully!")
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired reset token.");
    }

    //9- Update User
    @PutMapping("/update")
    public ResponseEntity<String> updateUser(Authentication authentication, @RequestParam String name, @RequestParam String password) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authenticated.");
        }
        //Retrieve the currently authenticated user from the security context.
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails userDetails) { //Extracts UserDetails from Authentication
            //Calls the service layer to update the user based on User Email.
            boolean success = userService.updateUser(userDetails.getUsername(), name, password);
            return success ? ResponseEntity.ok("User has been updated")
                    : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user data. Update failed");

        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An error has occurred!");
    }
}
