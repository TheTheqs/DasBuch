package com.example.base_server.service;

//Imports
import com.example.base_server.enums.Role;
import com.example.base_server.model.User;
import com.example.base_server.repository.UserRepository;
import com.example.base_server.utils.EmailValidator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    @Autowired //Dependency declaration
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder; //Bean that will make the hash
    @Autowired
    private AuthenticationManager authenticationManager;
    //Services
    //1- Register a new user (encrypts password and generates verification token)
    public User registerUser(String name, String email, String password, Role role){
        if (!EmailValidator.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format!");
        }

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists!");
        }

        String hashedPassword = passwordEncoder.encode(password); //Password cryptography
        String verificationToken = generateVerificationToken(); //Token generation

        User newUser = new User(name, email, hashedPassword, role); //User creation
        newUser.setVerificationToken(verificationToken); //Set token
        newUser.setTokenExpirationTime(LocalDateTime.now().plusMinutes(15)); //Set token time
        newUser.setIsActive(false); //Manipulate activation

        sendVerificationEmail(newUser); //Email verification sending
        return userRepository.save(newUser);
    }

    //2- Verify user email using the verification token
    public boolean verifyUser(String token){
        User user = userRepository.findByVerificationToken(token);
        if (user == null || user.getTokenExpirationTime().isBefore(LocalDateTime.now())) {
            return false; // invalid or expired token
        }
        user.setIsActive(true);
        user.setVerificationToken(null); //Make token null for security
        userRepository.save(user); //Update user state, which is now valid
        return true;
    }

    //3- Retrieve user by email (used for login)
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }

    //4- Login method (validates email and compares password)
    public User login(String email, String rawPassword) {
        User user = findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("No user found with this email."));

        if (!user.getIsActive()) {
            throw new IllegalStateException("User account is not activated. Please verify your email!");
        }

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials. Please try again.");
        }

        //Generates a token.
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(email, rawPassword);

        //Session begin with user authentication
        Authentication authentication = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        user.setPassword(null); // Remove password before returning for security reasons
        return user;
    }
    //5- Delete user by id
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User with ID " + id + " not found."));

        userRepository.deleteById(id);
    }

    //6- Send verification email simulator. This is for test phase only, in a production phase, this function must be done through email sending.
    private void sendVerificationEmail(User user) {
        System.out.println("📧 Simulated Email Sent:");
        System.out.println("To: " + user.getEmail());
        System.out.println("Subject: Verify your account");
        System.out.println("Body: Click the link to verify your account: http://localhost:8080/users/verify?token=" + user.getVerificationToken());
    }
    //7- Auxiliary method to generate a unique verification token
    private String generateVerificationToken() {
        return UUID.randomUUID().toString();
    }
}
