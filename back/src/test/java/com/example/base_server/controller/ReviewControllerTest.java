package com.example.base_server.controller;

import com.example.base_server.config.CustomUserDetails;
import com.example.base_server.dto.CreateReviewDTO;
import com.example.base_server.enums.Role;
import com.example.base_server.model.Author;
import com.example.base_server.model.Book;
import com.example.base_server.model.Review;
import com.example.base_server.model.User;
import com.example.base_server.repository.AuthorRepository;
import com.example.base_server.repository.BookRepository;
import com.example.base_server.repository.ReviewRepository;
import com.example.base_server.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    private User ownerUser;
    private User notOwnerUser;
    private User adminUser;
    private Book book;
    private Review review;

    @BeforeEach
    void setup() {
        reviewRepository.deleteAll();
        bookRepository.deleteAll();
        authorRepository.deleteAll();
        userRepository.deleteAll();

        Author author = authorRepository.save(new Author("Autor Teste", Set.of()));

        book = bookRepository.save(new Book("Livro Teste", Set.of(author)));

        ownerUser = userRepository.save(new User("Dono", "owner@example.com", "senha123", Role.USER));
        ownerUser.setIsActive(true);

        notOwnerUser = userRepository.save(new User("Outro", "notowner@example.com", "senha123", Role.USER));
        notOwnerUser.setIsActive(true);

        adminUser = userRepository.save(new User("Admin", "admin@example.com", "senha123", Role.ADMIN));
        adminUser.setIsActive(true);

        review = reviewRepository.save(new Review(ownerUser, book, "Synopsis", "Commentary", 8, LocalDateTime.now()));
    }

    @Test
    void shouldCreateReviewAsUser() throws Exception {
        CreateReviewDTO dto = new CreateReviewDTO("Livro Novo", List.of("Novo Autor"), "Sinopse", "Comentário", 4, LocalDateTime.now());

        mockMvc.perform(post("/reviews")
                        .with(user(new CustomUserDetails(ownerUser)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldRejectCreateReviewIfUnauthenticated() throws Exception {
        CreateReviewDTO dto = new CreateReviewDTO("Livro Novo", List.of("Novo Autor"), "Sinopse", "Comentário", 4, LocalDateTime.now());

        mockMvc.perform(post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldGetReviewByIdAsUser() throws Exception {
        mockMvc.perform(get("/reviews/" + review.getId())
                        .with(user(new CustomUserDetails(ownerUser))))
                .andExpect(status().isOk());
    }

    @Test
    void shouldRejectGetReviewByIdIfUnauthenticated() throws Exception {
        mockMvc.perform(get("/reviews/" + review.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldGetBookReviewsAsUser() throws Exception {
        mockMvc.perform(get("/reviews/book/" + book.getId())
                        .with(user(new CustomUserDetails(ownerUser))))
                .andExpect(status().isOk());
    }

    @Test
    void shouldRejectGetBookReviewsIfUnauthenticated() throws Exception {
        mockMvc.perform(get("/reviews/book/" + book.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldGetUserReviewsAsUser() throws Exception {
        mockMvc.perform(get("/reviews/user/" + ownerUser.getId())
                        .with(user(new CustomUserDetails(ownerUser))))
                .andExpect(status().isOk());
    }

    @Test
    void shouldRejectGetUserReviewsIfUnauthenticated() throws Exception {
        mockMvc.perform(get("/reviews/user/" + ownerUser.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldUpdateOwnReviewAsUser() throws Exception {
        CreateReviewDTO dto = new CreateReviewDTO("Livro Atualizado", List.of("Autor Teste"), "Nova Sinopse", "Novo Comentário", 4, LocalDateTime.now());
        System.out.println(review.getUser().toString());
        System.out.println(ownerUser.toString());
        System.out.println(review.getUser().equals(ownerUser));
        mockMvc.perform(patch("/reviews/" + review.getId())
                        .with(user(new CustomUserDetails(ownerUser)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldRejectUpdateReviewAsUserIfNotOwner() throws Exception {
        CreateReviewDTO dto = new CreateReviewDTO("Livro Atualizado", List.of("Autor Teste"), "Nova Sinopse", "Novo Comentário", 4, LocalDateTime.now());

        mockMvc.perform(patch("/reviews/" + review.getId())
                        .with(user(new CustomUserDetails(notOwnerUser)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldUpdateReviewAsAdmin() throws Exception {
        CreateReviewDTO dto = new CreateReviewDTO("Livro Atualizado", List.of("Autor Teste"), "Nova Sinopse", "Novo Comentário", 4, LocalDateTime.now());

        mockMvc.perform(patch("/reviews/" + review.getId())
                        .with(user(new CustomUserDetails(adminUser)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteOwnReviewAsUser() throws Exception {
        mockMvc.perform(delete("/reviews/" + review.getId())
                        .with(user(new CustomUserDetails(ownerUser))))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void shouldRejectDeleteReviewAsUserIfNotOwner() throws Exception {
        mockMvc.perform(delete("/reviews/" + review.getId())
                        .with(user(new CustomUserDetails(notOwnerUser))))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldDeleteReviewAsAdmin() throws Exception {
        mockMvc.perform(delete("/reviews/" + review.getId())
                        .with(user(new CustomUserDetails(adminUser))))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}

