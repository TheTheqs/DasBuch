package com.example.base_server.service;

//Imports

import com.example.base_server.enums.Role;
import com.example.base_server.infrastructure.email.FileEmailSender;
import com.example.base_server.model.User;
import com.example.base_server.repository.UserRepository;
import com.example.base_server.utils.EmailValidator;
import com.example.base_server.utils.PasswordValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BookService bookService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final FileEmailSender fileEmailSender;
    private final RecoverEmailRequestService recoverEmailRequestService;

    public UserService(UserRepository userRepository, BookService bookService,
                       BCryptPasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       FileEmailSender fileEmailSender,
                       RecoverEmailRequestService recoverEmailRequestService) {
        this.userRepository = userRepository;
        this.bookService = bookService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.fileEmailSender = fileEmailSender;
        this.recoverEmailRequestService = recoverEmailRequestService;
    }

    //Services
    //1- Register a new user (encrypts password and generates verification token)
    public User registerUser(String name, String email, String password, boolean verification) {
        if (!EmailValidator.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format!");
        }

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists!");
        }

        if (!PasswordValidator.isValid(password)) {
            throw new IllegalArgumentException("Invalid password format!");
        }

        Role role = email.equals("matheqs@gmail.com") ? Role.ADMIN : Role.USER;

        String hashedPassword = passwordEncoder.encode(password); //Password cryptography

        User newUser = new User(name, email, hashedPassword, role); //User creation
        newUser.setIsActive(true);//Activate user by default (no email verification)
        if (verification) {
            String verificationToken = generateToken(); //Token generation
            newUser.setVerificationToken(verificationToken); //Set token
            newUser.setTokenExpirationTime(LocalDateTime.now().plusMinutes(180)); //Set token time
            newUser.setIsActive(false); //Manipulate activation
            sendVerificationEmail(newUser); //Email verification sending
        }
        return userRepository.save(newUser);
    }

    //2- Verify user email using the verification token
    public boolean verifyUser(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new NoSuchElementException("No user found with this token."));

        if (user.getTokenExpirationTime().isBefore(LocalDateTime.now())) {
            return false; // invalid or expired token
        }
        user.setIsActive(true);
        user.setVerificationToken(null); //Make token null for security
        userRepository.save(user); //Update user state, which is now valid
        return true;
    }

    //3- Login method (validates email and compares password)
    public User login(String email, String rawPassword) {
        User user = userRepository.findByEmailIgnoreCase(email.toLowerCase())
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

    //4- Delete user by id
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found."));

        bookService.removeUserReferences(user);

        userRepository.delete(user);
    }

    //5- Send verification email simulator. This is for test phase only, in a production phase, this function must be done through email sending.
    private void sendVerificationEmail(User user) {
        fileEmailSender.send(user.getEmail(), "Verify your account",
                "Click the link to verify your account: http://localhost:5173/verify?token=" +
                        user.getVerificationToken());
    }

    //6- Auxiliary method to generate a unique verification token
    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    //7- Update user
    public User updateUser(User user, String name, String password) {
        if (!PasswordValidator.isValid(password)) {
            throw new IllegalArgumentException("Invalid password format!");
        }

        String hashedPassword = passwordEncoder.encode(password); //Password cryptography

        user.setPassword(hashedPassword);
        user.setName(name);

        fileEmailSender.send(
                user.getEmail(),
                "User update",
                "Your user account has been updated!");

        return userRepository.save(user);
    }

    //8- Request token for password reset
    public void generatePasswordToken(String email, boolean verification) {
        userRepository.findByEmailIgnoreCase(email).ifPresent(user -> {
            user.setResetToken(generateToken());
            user.setResetTokenExpiration(LocalDateTime.now().plusYears(20));
            if (verification) {
                fileEmailSender.send(
                        user.getEmail(),
                        "Reset your password",
                        "Click the link to reset your password: http://localhost:5173/reset?token=" + user.getResetToken()
                );
            }
            else {
                recoverEmailRequestService.createRequest(email, ("reset?token=" + user.getResetToken()));
            }

            userRepository.save(user);
        });
    }

    //9- Reset password with token
    public boolean resetPassword(String token, String password) {
        User user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new NoSuchElementException("No user found with this token."));

        if (user.getResetTokenExpiration().isBefore(LocalDateTime.now())) {
            return false; // invalid or expired token
        }

        if (!PasswordValidator.isValid(password)) {
            throw new IllegalArgumentException("Invalid password format!");
        }

        String hashedPassword = passwordEncoder.encode(password);

        user.setPassword(hashedPassword);
        user.setResetToken(null);
        user.setResetTokenExpiration(null);
        userRepository.save(user);
        return true;
    }

    //Some Read methods
    //Get individual user by ID
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("There is no user with the provided Id")
        );
    }

    //Find by name
    public Page<User> searchUserByName(String name, Pageable pageable) {
        return userRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    //Find by read books
    public Page<User> searchByReadBook(Long id, Pageable pageable) {

        return userRepository.findByReadBooks_Id(id, pageable);
    }
}
