package com.example.base_server.service;

import com.example.base_server.enums.Role;
import com.example.base_server.model.Author;
import com.example.base_server.model.Book;
import com.example.base_server.model.User;
import com.example.base_server.repository.AuthorRepository;
import com.example.base_server.repository.BookRepository;
import com.example.base_server.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class BookServiceTest {

    @Autowired private BookService bookService;
    @Autowired private AuthorService authorService;
    @Autowired private BookRepository bookRepository;
    @Autowired private AuthorRepository authorRepository;
    @Autowired private UserRepository userRepository;

    @Test
    void shouldCreateNewBook() {
        Book book = bookService.createBook("Duna", List.of("Frank Herbert"));
        assertNotNull(book.getId());
        assertEquals("Duna", book.getTitle());
        assertEquals(1, book.getAuthors().size());
    }

    @Test
    void shouldReturnExistingBookIfDuplicateTitleAndAuthors() {
        bookService.createBook("Duna", List.of("Frank Herbert"));
        Book book2 = bookService.createBook("Duna", List.of("Frank Herbert"));

        assertEquals(1, bookRepository.findAll().size());
        assertEquals("Duna", book2.getTitle());
    }

    @Test
    void shouldFindBookById() {
        Book saved = bookService.createBook("1984", List.of("George Orwell"));
        Book found = bookService.getBookById(saved.getId());

        assertEquals(saved.getTitle(), found.getTitle());
    }

    @Test
    void shouldThrowWhenBookNotFoundById() {
        assertThrows(NoSuchElementException.class, () -> {
            bookService.getBookById(999L);
        });
    }

    @Test
    void shouldSearchBooksByPartialTitle() {
        bookService.createBook("Duna", List.of("Frank Herbert"));
        bookService.createBook("Duna Messias", List.of("Frank Herbert"));
        bookService.createBook("Fundação", List.of("Isaac Asimov"));

        Page<Book> result = bookService.getBookListByTitle("una", PageRequest.of(0, 10));
        assertEquals(2, result.getContent().size());
    }

    @Test
    void shouldListBooksByAuthor() {
        Author a1 = authorService.createAuthor("Asimov");
        Author a2 = authorService.createAuthor("Herbert");

        bookService.createBook("Fundação", List.of("Asimov"));
        bookService.createBook("Duna", List.of("Herbert"));
        bookService.createBook("Prelúdio à Fundação", List.of("Asimov"));

        Page<Book> asimovBooks = bookService.getBookListByAuthor(a1.getId(), PageRequest.of(0, 10));
        assertEquals(2, asimovBooks.getContent().size());
    }

    @Test
    void shouldListBooksByUser() {
        // Setup
        User user = userRepository.save(new User("John", "john@test.com", "123456", Role.USER));
        Book book = bookService.createBook("Neuromancer", List.of("William Gibson"));
        Set<User> users = new HashSet<>();
        users.add(user);
        book.setReadBy(users);
        bookRepository.save(book);

        // Test
        Page<Book> page = bookService.getBookListByUser(user.getId(), PageRequest.of(0, 10));
        assertEquals(1, page.getContent().size());
        assertEquals("Neuromancer", page.getContent().get(0).getTitle());
    }

    @Test
    void shouldUpdateBookTitleAndAuthors() {
        Book book = bookService.createBook("Velho Título", List.of("Autor Um"));
        Author newAuthor = authorService.createAuthor("Autor Dois");
        Set<Author> newAuthors = new HashSet<>();
        newAuthors.add(newAuthor);
        Book updated = bookService.updateBook(book.getId(), "Novo Título", newAuthors);

        assertEquals("Novo Título", updated.getTitle());
        assertTrue(updated.getAuthors().contains(newAuthor));
    }

    @Test
    void shouldUpdateOnlyBookTitle() {
        Book book = bookService.createBook("Original", List.of("Autor A"));

        Book updated = bookService.updateBook(book.getId(), "Atualizado", null);

        assertEquals("Atualizado", updated.getTitle());
        assertEquals(1, updated.getAuthors().size());
    }

    @Test
    void shouldThrowWhenUpdatingNonexistentBook() {
        Author a = authorService.createAuthor("Qualquer");
        Set<Author> authors = new HashSet<>();
        authors.add(a);
        assertThrows(NoSuchElementException.class, () -> {
            bookService.updateBook(999L, "Novo", authors);
        });
    }

    @Test
    void shouldDeleteExistingBook() {
        Book book = bookService.createBook("Livro Apagável", List.of("Autor X"));

        boolean result = bookService.deleteBook(book.getId());

        assertTrue(result);
        assertFalse(bookRepository.existsById(book.getId()));
    }

    @Test
    void shouldReturnFalseWhenDeletingNonexistentBook() {
        boolean result = bookService.deleteBook(9876L);
        assertFalse(result);
    }
}

