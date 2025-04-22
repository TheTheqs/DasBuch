package com.example.base_server.controller;

import com.example.base_server.enums.Role;
import com.example.base_server.model.Author;
import com.example.base_server.model.User;
import com.example.base_server.repository.AuthorRepository;
import com.example.base_server.repository.UserRepository;
import com.example.base_server.config.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private UserRepository userRepository;

    private User adminUser;
    private User normalUser;
    private Author savedAuthor;

    @BeforeEach
    void setup() {
        authorRepository.deleteAll();
        userRepository.deleteAll();

        savedAuthor = authorRepository.save(new Author("Gabriel García Márquez", new HashSet<>()));

        adminUser = new User("Admin", "admin@example.com", "senha123", Role.ADMIN);
        adminUser.setIsActive(true);
        userRepository.save(adminUser);

        normalUser = new User("User", "user@example.com", "senha123", Role.USER);
        normalUser.setIsActive(true);
        userRepository.save(normalUser);
    }

    private void authenticate(User user) {
        CustomUserDetails userDetails = new CustomUserDetails(user);
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authToken);
        SecurityContextHolder.setContext(context);
    }

    @Test
    void shouldGetAuthorByIdAsUser() throws Exception {
        mockMvc.perform(get("/authors/" + savedAuthor.getId())
                        .with(user(new CustomUserDetails(normalUser))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Gabriel García Márquez"));
    }

    @Test
    void shouldSearchAuthorsByNameAsUser() throws Exception {
        mockMvc.perform(get("/authors/search")
                        .param("name", "gabriel")
                        .with(user(new CustomUserDetails(normalUser))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Gabriel García Márquez"));
    }

    @Test
    void shouldRejectGetAuthorByIdIfUnauthenticated() throws Exception {
        mockMvc.perform(get("/authors/" + savedAuthor.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRejectSearchAuthorsByNameIfUnauthenticated() throws Exception {
        mockMvc.perform(get("/authors/search")
                        .param("name", "gabriel"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldUpdateAuthorAsAdmin() throws Exception {
        authenticate(adminUser);
        mockMvc.perform(patch("/authors/" + savedAuthor.getId())
                        .param("name", "Gabo Atualizado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Gabo Atualizado"));
    }

    @Test
    void shouldRejectUpdateAuthorAsUser() throws Exception {
        authenticate(normalUser);
        mockMvc.perform(patch("/authors/" + savedAuthor.getId())
                        .with(user(new CustomUserDetails(normalUser)))
                        .param("name", "Nome Novo"))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldDeleteAuthorAsAdmin() throws Exception {
        authenticate(adminUser);
        mockMvc.perform(delete("/authors/" + savedAuthor.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void shouldRejectDeleteAuthorAsUser() throws Exception {
        mockMvc.perform(delete("/authors/" + savedAuthor.getId())
                        .with(user(new CustomUserDetails(normalUser))))
                .andExpect(status().isForbidden());
    }
}

