package com.example.base_server.model;

import com.example.base_server.repository.AuthorRepository;
import com.example.base_server.repository.BookRepository;
import com.example.base_server.repository.ReviewRepository;
import com.example.base_server.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

//Integration test.
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class BookTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    void shouldPersistBookAndGenerateTimestamps() {
        // Arrange
        Author author = authorRepository.save(new Author("Machado de Assis", new HashSet<>()));
        Set<Author> authors = Set.of(author);

        Book book = new Book("Dom Casmurro", authors, new HashSet<>());

        // Act
        bookRepository.save(book);
        em.flush();

        // Assert
        assertNotNull(book.getId());
        assertNotNull(book.getCreatedAt());
        assertNotNull(book.getUpdatedAt());
    }

    @Test
    void shouldCascadeSaveReviews() {
        // Arrange
        Author author = authorRepository.save(new Author("Clarice Lispector", new HashSet<>()));
        Book book = new Book("A Hora da Estrela", Set.of(author), new HashSet<>());

        Review review = new Review();
        review.setScore(5);
        review.setCommentary("Obra-prima");
        review.setUser(new User("user3@email.com", "User Two"));
        review.setBook(book);
        book.getReviews().add(review);

        // Act
        bookRepository.save(book);
        em.flush();

        // Assert
        assertNotNull(review.getId());
        assertEquals(1, reviewRepository.findAll().size());
    }


    @Test
    void shouldPersistBookWithMultipleUsers() {
        // Arrange
        Author author = authorRepository.save(new Author("Margaret Atwood", new HashSet<>()));
        Set<Author> authors = Set.of(author);

        User user1 = userRepository.save(new User("user1@email.com", "User One"));
        User user2 = userRepository.save(new User("user2@email.com", "User Two"));
        Set<User> users = Set.of(user1, user2);

        Book book = new Book("O Conto da Aia", authors, new HashSet<>());
        book.setReadBy(users);

        // Act
        bookRepository.save(book);
        em.flush();

        // Assert
        Book savedBook = bookRepository.findById(book.getId()).orElseThrow();
        assertEquals(2, savedBook.getReadBy().size());
    }
}

