package com.example.base_server.service;

import com.example.base_server.enums.Role;
import com.example.base_server.model.User;
import com.example.base_server.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UserServiceTest {

    @Autowired private UserService userService;
    @Autowired private UserRepository userRepository;
    @Autowired private BCryptPasswordEncoder passwordEncoder;

    private String validName = "João da Silva";
    private String validEmail = "joao@example.com";
    private String validPassword = "Senha123!";
    private String invalidEmail = "joao.com";
    private String weakPassword = "123";

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void shouldRegisterUserWithValidData() {
        User user = userService.registerUser(validName, validEmail, validPassword, Role.USER);

        assertNotNull(user.getId());
        assertFalse(user.getIsActive());
        assertNotNull(user.getVerificationToken());
        assertTrue(passwordEncoder.matches(validPassword, user.getPassword()));
    }

    @Test
    void shouldThrowForInvalidEmail() {
        assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(validName, invalidEmail, validPassword, Role.USER);
        });
    }

    @Test
    void shouldThrowForExistingEmail() {
        userService.registerUser(validName, validEmail, validPassword, Role.USER);

        assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser("Outro", validEmail, validPassword, Role.USER);
        });
    }

    @Test
    void shouldThrowForWeakPassword() {
        assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(validName, validEmail, weakPassword, Role.USER);
        });
    }

    @Test
    void shouldVerifyUserSuccessfully() {
        User user = userService.registerUser(validName, validEmail, validPassword, Role.USER);
        boolean result = userService.verifyUser(user.getVerificationToken());

        assertTrue(result);
        assertTrue(userRepository.findById(user.getId()).get().getIsActive());
    }

    @Test
    void shouldReturnFalseForExpiredVerificationToken() {
        User user = userService.registerUser(validName, validEmail, validPassword, Role.USER);
        user.setTokenExpirationTime(LocalDateTime.now().minusMinutes(1));
        userRepository.save(user);

        boolean result = userService.verifyUser(user.getVerificationToken());
        assertFalse(result);
    }

    @Test
    void shouldThrowWhenTokenNotFound() {
        assertThrows(NoSuchElementException.class, () -> {
            userService.verifyUser("token-invalido");
        });
    }

    @Test
    void shouldLoginSuccessfully() {
        User user = userService.registerUser(validName, validEmail, validPassword, Role.USER);
        user.setIsActive(true);
        userRepository.save(user);

        User logged = userService.login(validEmail, validPassword);
        assertEquals(validEmail, logged.getEmail());
        assertNull(logged.getPassword());
    }

    @Test
    void shouldNotLoginIfNotActivated() {
        userService.registerUser(validName, validEmail, validPassword, Role.USER);

        assertThrows(IllegalStateException.class, () -> {
            userService.login(validEmail, validPassword);
        });
    }

    @Test
    void shouldThrowForWrongPassword() {
        User user = userService.registerUser(validName, validEmail, validPassword, Role.USER);
        user.setIsActive(true);
        userRepository.save(user);

        assertThrows(BadCredentialsException.class, () -> {
            userService.login(validEmail, "senhaErrada");
        });
    }

    @Test
    void shouldThrowForUnknownEmail() {
        assertThrows(NoSuchElementException.class, () -> {
            userService.login("inexistente@email.com", validPassword);
        });
    }

    @Test
    void shouldDeleteUser() {
        User user = userService.registerUser(validName, validEmail, validPassword, Role.USER);
        userService.deleteUser(user.getId());

        assertFalse(userRepository.existsById(user.getId()));
    }

    @Test
    void shouldThrowWhenDeletingUnknownUser() {
        assertThrows(NoSuchElementException.class, () -> {
            userService.deleteUser(999L);
        });
    }

    @Test
    void shouldUpdateUser() {
        User user = userService.registerUser(validName, validEmail, validPassword, Role.USER);
        String newName = "João Atualizado";
        String newPassword = "NovaSenha123!";

        User updated = userService.updateUser(user, newName, newPassword);

        assertEquals(newName, updated.getName());
        assertEquals(newPassword, updated.getPassword());
    }

    @Test
    void shouldGenerateResetToken() {
        userService.registerUser(validName, validEmail, validPassword, Role.USER);
        userService.generatePasswordToken(validEmail);

        User user = userRepository.findByEmail(validEmail).get();
        assertNotNull(user.getResetToken());
        assertNotNull(user.getResetTokenExpiration());
    }

    @Test
    void shouldResetPasswordSuccessfully() {
        User user = userService.registerUser(validName, validEmail, validPassword, Role.USER);
        userService.generatePasswordToken(validEmail);

        String token = userRepository.findByEmail(validEmail).get().getResetToken();
        boolean result = userService.resetPassword(token, "Senha321!");

        assertTrue(result);
        assertNull(userRepository.findByEmail(validEmail).get().getResetToken());
    }

    @Test
    void shouldReturnFalseIfResetTokenExpired() {
        User user = userService.registerUser(validName, validEmail, validPassword, Role.USER);
        user.setResetToken("exp-token");
        user.setResetTokenExpiration(LocalDateTime.now().minusMinutes(10));
        userRepository.save(user);

        boolean result = userService.resetPassword("exp-token", "Senha123!");
        assertFalse(result);
    }

    @Test
    void shouldThrowIfResetTokenNotFound() {
        assertThrows(NoSuchElementException.class, () -> {
            userService.resetPassword("token-invalido", "Senha321!");
        });
    }

    @Test
    void shouldThrowIfResetPasswordIsWeak() {
        User user = userService.registerUser(validName, validEmail, validPassword, Role.USER);
        userService.generatePasswordToken(validEmail);
        String token = userRepository.findByEmail(validEmail).get().getResetToken();

        assertThrows(IllegalArgumentException.class, () -> {
            userService.resetPassword(token, weakPassword);
        });
    }

    @Test
    void shouldSearchByName() {
        userService.registerUser("Maria Teste", "maria@test.com", validPassword, Role.USER);

        Page<User> result = userService.searchUserByName("maria", PageRequest.of(0, 10));
        assertEquals(1, result.getContent().size());
    }

    @Test
    void shouldSearchByReadBook() {
        // Este teste assume relacionamento com livro, deve ser completado futuramente
        Page<User> result = userService.searchByReadBook(1L, PageRequest.of(0, 10));
        assertNotNull(result);
    }
}

