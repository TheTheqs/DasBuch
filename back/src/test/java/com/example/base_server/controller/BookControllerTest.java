package com.example.base_server.controller;

import com.example.base_server.config.CustomUserDetails;
import com.example.base_server.enums.Role;
import com.example.base_server.model.Author;
import com.example.base_server.model.Book;
import com.example.base_server.model.User;
import com.example.base_server.repository.AuthorRepository;
import com.example.base_server.repository.BookRepository;
import com.example.base_server.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private UserRepository userRepository;

    private User adminUser;
    private User normalUser;
    private Book savedBook;

    @BeforeEach
    void setup() {
        bookRepository.deleteAll();
        authorRepository.deleteAll();
        userRepository.deleteAll();

        Author author = authorRepository.save(new Author("Autor Teste", new HashSet<>()));
        savedBook = bookRepository.save(new Book("Livro Teste", Set.of(author)));

        adminUser = new User("Admin", "admin@example.com", "senha123", Role.ADMIN);
        adminUser.setIsActive(true);
        userRepository.save(adminUser);

        normalUser = new User("User", "user@example.com", "senha123", Role.USER);
        normalUser.setIsActive(true);
        userRepository.save(normalUser);
    }

    @Test
    void shouldGetBookByIdAsUser() throws Exception {
        mockMvc.perform(get("/books/" + savedBook.getId())
                        .with(user(new CustomUserDetails(normalUser))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Livro Teste"));
    }

    @Test
    void shouldRejectGetBookByIdIfUnauthenticated() throws Exception {
        mockMvc.perform(get("/books/" + savedBook.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldSearchBooksByTitleAsUser() throws Exception {
        mockMvc.perform(get("/books")
                        .param("title", "livro")
                        .with(user(new CustomUserDetails(normalUser))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Livro Teste"));
    }

    @Test
    void shouldRejectSearchBooksByTitleIfUnauthenticated() throws Exception {
        mockMvc.perform(get("/books")
                        .param("title", "livro"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldGetBooksByAuthorAsUser() throws Exception {
        mockMvc.perform(get("/books/author/" + savedBook.getAuthors().iterator().next().getId())
                        .with(user(new CustomUserDetails(normalUser))))
                .andExpect(status().isOk());
    }

    @Test
    void shouldRejectGetBooksByAuthorIfUnauthenticated() throws Exception {
        mockMvc.perform(get("/books/author/" + savedBook.getAuthors().iterator().next().getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldGetBooksByUserAsUser() throws Exception {
        mockMvc.perform(get("/books/user/" + normalUser.getId())
                        .with(user(new CustomUserDetails(normalUser))))
                .andExpect(status().isOk());
    }

    @Test
    void shouldRejectGetBooksByUserIfUnauthenticated() throws Exception {
        mockMvc.perform(get("/books/user/" + normalUser.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldUpdateBookAsAdmin() throws Exception {
        String json = "{\"title\": \"Livro Atualizado\", \"authorIds\": []}";

        mockMvc.perform(patch("/books/" + savedBook.getId())
                        .with(user(new CustomUserDetails(adminUser)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Livro Atualizado"));
    }

    @Test
    void shouldRejectUpdateBookAsUser() throws Exception {
        String json = "{\"title\": \"Tentativa de Update\", \"authorIds\": []}";

        mockMvc.perform(patch("/books/" + savedBook.getId())
                        .with(user(new CustomUserDetails(normalUser)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldDeleteBookAsAdmin() throws Exception {
        mockMvc.perform(delete("/books/" + savedBook.getId())
                        .with(user(new CustomUserDetails(adminUser))))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void shouldRejectDeleteBookAsUser() throws Exception {
        mockMvc.perform(delete("/books/" + savedBook.getId())
                        .with(user(new CustomUserDetails(normalUser))))
                .andExpect(status().isForbidden());
    }
}
