package com.example.base_server.service;

//Imports
import com.example.base_server.enums.Role;
import com.example.base_server.model.User;
import com.example.base_server.repository.UserRepository;
import com.example.base_server.utils.EmailValidator;
import com.example.base_server.utils.NameValidator;
import com.example.base_server.utils.PasswordValidator;
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
    @Autowired
    private EmailSender emailSender;
    //Services
    //1- Register a new user (encrypts password and generates verification token)
    public User registerUser(String name, String email, String password, Role role){
        if (!EmailValidator.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format!");
        }
        //Name validation
        String nameMessage = NameValidator.isValid(name);
        if (nameMessage != null){
            throw new IllegalArgumentException(nameMessage);
        }

        //Password validation
        String passMessage = PasswordValidator.isValid(password);
        if (passMessage != null){
            throw new IllegalArgumentException(passMessage);
        }

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists!");
        }

        String hashedPassword = passwordEncoder.encode(password); //Password cryptography

        User newUser = new User(name.trim(), email, hashedPassword, role); //User creation
        generateVerificationToken(newUser); //Set token
        newUser.setIsActive(false); //Manipulate activation
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
        emailSender.sendEmail(user.getEmail(),
                "Verify your account",
                "Click the link to verify your account: http://localhost:8080/users/verify?token=" + user.getVerificationToken());
    }

    //7- Auxiliary method to generate a unique verification token
    private void generateVerificationToken(User user) {
        user.setVerificationToken(UUID.randomUUID().toString());
        user.setTokenExpirationTime(LocalDateTime.now().plusMinutes(60));
        sendVerificationEmail(user); //Email verification sending
    }

    //8- Reset user password using reset token
    public boolean resetPassword(String token, String password){
        User user = userRepository.findByResetToken(token);
        if (user == null || user.getResetTokenExpirationTime().isBefore(LocalDateTime.now())) {
            return false; // invalid or expired token
        }
        //Password Validation
        String passMessage = PasswordValidator.isValid(password);
        if (passMessage != null){
            throw new IllegalArgumentException(passMessage);
        }
        //Hashing password
        String hashedPassword = passwordEncoder.encode(password);
        user.setPassword(hashedPassword);
        user.setResetToken(null); //Make token null for security
        userRepository.save(user); //Update user password, which is now valid
        return true;
    }

    //9- Request password reset.
    public void requestReset(String email) {
        Optional<User> optionalUser = findByEmail(email);
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            user.setResetToken(UUID.randomUUID().toString());
            user.setResetTokenExpirationTime(LocalDateTime.now().plusMinutes(15));
            sendResetEmail(user); //Email verification sending
            userRepository.save(user);
        }
        else {
            throw new NoSuchElementException("No user found with the provided email!");
        }
    }

    //10- Send reset token message
    private void sendResetEmail(User user) {
        emailSender.sendEmail(user.getEmail(),
                "Reset password",
                "Click the link to reset the password of your account: http://localhost:8080/users/reset?token=" + user.getResetToken());
    }

    //11- Update User
    public boolean updateUser(String email, String name, String password){
        Optional<User> optionalUser = findByEmail(email);
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            //Name validation
            String nameMessage = NameValidator.isValid(name);
            if (nameMessage != null){
                throw new IllegalArgumentException(nameMessage);
            }

            //Password validation
            String passMessage = PasswordValidator.isValid(password);
            if (passMessage != null){
                throw new IllegalArgumentException(passMessage);
            }
            String hashedPassword = passwordEncoder.encode(password); //Password cryptography
            //Update
            user.setName(name.trim());
            user.setPassword(hashedPassword);

            userRepository.save(user);
            return true;
        }
        else {
            throw new NoSuchElementException("No user found with the provided email!");
        }
    }
}
