package com.example.base_server.service;

import com.example.base_server.enums.Role;
import com.example.base_server.model.Book;
import com.example.base_server.model.Review;
import com.example.base_server.model.User;
import com.example.base_server.repository.BookRepository;
import com.example.base_server.repository.ReviewRepository;
import com.example.base_server.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ReviewServiceTest {

    @Autowired private ReviewService reviewService;
    @Autowired private BookRepository bookRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ReviewRepository reviewRepository;

    private User user;
    private User admin;
    private Book book;

    @BeforeEach
    void setup() {
        reviewRepository.deleteAll();
        bookRepository.deleteAll();
        userRepository.deleteAll();

        user = new User("User", "user@example.com", "123", Role.USER);
        admin = new User("Admin", "admin@example.com", "123", Role.ADMIN);
        book = new Book("Book Title", null, null);

        user = userRepository.save(user);
        admin = userRepository.save(admin);
        book = bookRepository.save(book);
    }

    @Test
    void shouldCreateReview() {
        Review review = reviewService.createReview(user, book, "Resumo", "Comentário", 4, LocalDateTime.now());
        assertNotNull(review.getId());
    }

    @Test
    void shouldNotAllowDuplicateReview() {
        reviewService.createReview(user, book, "Resumo", "Comentário", 4, LocalDateTime.now());
        assertThrows(IllegalStateException.class, () -> {
            reviewService.createReview(user, book, "Outro", "Outro", 5, LocalDateTime.now());
        });
    }

    @Test
    void shouldGetReviewById() {
        Review review = reviewService.createReview(user, book, "Resumo", "Comentário", 4, LocalDateTime.now());
        Review found = reviewService.getReview(review.getId());
        assertEquals(review.getId(), found.getId());
    }

    @Test
    void shouldThrowWhenReviewNotFound() {
        assertThrows(Exception.class, () -> reviewService.getReview(999L));
    }

    @Test
    void shouldGetBookReviews() {
        reviewService.createReview(user, book, "Resumo", "Comentário", 4, LocalDateTime.now());
        Page<Review> result = reviewService.getBookReviews(book.getId(), PageRequest.of(0, 10));
        assertEquals(1, result.getContent().size());
    }

    @Test
    void shouldThrowIfBookDoesNotExist() {
        assertThrows(Exception.class, () -> reviewService.getBookReviews(999L, PageRequest.of(0, 10)));
    }

    @Test
    void shouldGetUserReviews() {
        reviewService.createReview(user, book, "Resumo", "Comentário", 4, LocalDateTime.now());
        Page<Review> result = reviewService.getUserReviews(user.getId(), PageRequest.of(0, 10));
        assertEquals(1, result.getContent().size());
    }

    @Test
    void shouldThrowIfUserDoesNotExist() {
        assertThrows(Exception.class, () -> reviewService.getUserReviews(999L, PageRequest.of(0, 10)));
    }

    @Test
    void shouldUpdateReviewByOwner() {
        Review review = reviewService.createReview(user, book, "Resumo", "Comentário", 4, LocalDateTime.now());
        Review updated = reviewService.updateReview(user, review.getId(), book, "Novo Resumo", "Novo Comentário", 5, LocalDateTime.now());
        assertEquals("Novo Resumo", updated.getSynopsys());
    }

    @Test
    void shouldNotUpdateIfNotOwner() {
        Review review = reviewService.createReview(user, book, "Resumo", "Comentário", 4, LocalDateTime.now());
        assertThrows(BadCredentialsException.class, () -> reviewService.updateReview(admin, review.getId(), book, "X", "Y", 2, LocalDateTime.now()));
    }

    @Test
    void shouldDeleteReviewByOwner() {
        Review review = reviewService.createReview(user, book, "Resumo", "Comentário", 4, LocalDateTime.now());
        boolean deleted = reviewService.deleteReview(review.getId(), user);
        assertTrue(deleted);
    }

    @Test
    void shouldDeleteReviewByAdmin() {
        Review review = reviewService.createReview(user, book, "Resumo", "Comentário", 4, LocalDateTime.now());
        boolean deleted = reviewService.deleteReview(review.getId(), admin);
        assertTrue(deleted);
    }

    @Test
    void shouldNotDeleteIfNotOwnerOrAdmin() {
        User other = userRepository.save(new User("Other", "other@mail.com", "pass", Role.USER));
        Review review = reviewService.createReview(user, book, "Resumo", "Comentário", 4, LocalDateTime.now());
        assertThrows(BadCredentialsException.class, () -> reviewService.deleteReview(review.getId(), other));
    }

    @Test
    void shouldReturnFalseWhenDeletingNonexistentReview() {
        boolean result = reviewService.deleteReview(999L, user);
        assertFalse(result);
    }
}
