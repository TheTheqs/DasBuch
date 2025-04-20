package com.example.base_server.controller;

import com.example.base_server.config.CustomUserDetails;
import com.example.base_server.enums.Role;
import com.example.base_server.infrastructure.email.FileEmailSender;
import com.example.base_server.model.User;
import com.example.base_server.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;
    @Autowired private ObjectMapper objectMapper;

    private final String email = "user@email.com";
    private final String password = "Senha123!";

    @TestConfiguration
    static class FileEmailSenderTestConfig {
        @Bean
        public FileEmailSender fileEmailSender() {
            return new FileEmailSender() {
                @Override
                public void send(String to, String subject, String content) {
                }
            };
        }
    }

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }

    @Test
    void shouldRegisterUser() throws Exception {
        mockMvc.perform(post("/users/register")
                        .param("name", "João")
                        .param("email", email)
                        .param("password", password))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(email));
    }

    @Test
    void shouldVerifyUserWithInvalidToken() throws Exception {
        mockMvc.perform(get("/users/verify")
                        .param("token", "invalid-token"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No user found with this token."));
    }

    @Test
    void shouldTriggerForgotPassword() throws Exception {
        mockMvc.perform(post("/users/forgot-password")
                        .param("email", "any@email.com"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldTryResetPasswordWithInvalidToken() throws Exception {
        mockMvc.perform(patch("/users/reset")
                        .param("token", "fake-token")
                        .param("password", "NewPass123!"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No user found with this token."));
    }

    @Test
    void shouldAccessAuthenticatedRoute() throws Exception {
        User user = authenticateAs();

        mockMvc.perform(get("/users/me")
                .with(user(new CustomUserDetails(user))))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(user.getEmail())));
    }

    @Test
    void shouldRejectUnauthenticatedAccessToMe() throws Exception {
        mockMvc.perform(get("/users/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "joao", roles = {"USER"})
    void shouldUpdateUserAttributes() throws Exception {
        User user = authenticateAs();

        mockMvc.perform(patch("/users/update")
                        .with(user(new CustomUserDetails(user)))
                        .param("name", "João Atualizado")
                        .param("password", "NovaSenha123!"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("João Atualizado"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldDeleteUserAsAdmin() throws Exception {
        User user = new User("Alvo", "alvo@email.com", "senha", Role.USER);
        user = userRepository.save(user);

        mockMvc.perform(delete("/users/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully."));
    }

    @Test
    void shouldRejectUnauthorizedDelete() throws Exception {
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldSearchUsersByName() throws Exception {
        User user = authenticateAs(); // já salva, ativado e autenticado

        mockMvc.perform(get("/users/search")
                        .param("name", user.getName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    @WithMockUser
    void shouldLogoutSuccessfully() throws Exception {
        mockMvc.perform(get("/users/logout"))
                .andExpect(status().isOk())
                .andExpect(content().string("You have been logged out successfully!"));
    }

    //Util function: Creating a valid user
    private User authenticateAs() {
        User user = new User("Matheqs", "matheqs@email.com", "Senha123!", Role.USER);
        user.setReviews(new HashSet<>());
        user.setIsActive(true);
        user.setVerificationToken("123");
        user.setTokenExpirationTime(LocalDateTime.now().plusMinutes(10));
        user.setIsActive(true);
        userRepository.save(user);

        CustomUserDetails userDetails = new CustomUserDetails(user);
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authToken);
        SecurityContextHolder.setContext(context);
        return user;
    }
}

