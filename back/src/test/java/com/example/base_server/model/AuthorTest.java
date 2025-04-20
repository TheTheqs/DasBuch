package com.example.base_server.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class AuthorTest {

    @Test
    void testConstructorAndGetters() {
        Set<Book> books = new HashSet<>();
        Author author = new Author("George Orwell", books);

        assertEquals("George Orwell", author.getName());
        assertEquals(books, author.getBooks());
    }

    @Test
    void testSetters() {
        Author author = new Author();
        author.setName("Octavia E. Butler");
        Set<Book> bookSet = new HashSet<>();
        author.setBooks(bookSet);

        assertEquals("Octavia E. Butler", author.getName());
        assertEquals(bookSet, author.getBooks());
    }

    @Test
    void testAddAndRemoveBook() {
        Author author = new Author();
        Book book = new Book();

        author.setBooks(new HashSet<>());
        author.addBook(book);

        assertTrue(author.getBooks().contains(book));

        author.removeBook(book);
        assertFalse(author.getBooks().contains(book));
    }

    @Test
    void testEqualsAndHashCode() {
        Author author1 = new Author();
        author1.setName("Toni Morrison");

        Author author2 = new Author();
        author2.setName("Toni Morrison");

        assertEquals(author1, author2);
        assertEquals(author1.hashCode(), author2.hashCode());
    }

    @Test
    void testPrePersistSetsTimestamps() {
        Author author = new Author();
        author.onCreate(); // simula @PrePersist

        assertNotNull(author.getCreatedAt());
        assertNotNull(author.getUpdatedAt());
        assertEquals(author.getCreatedAt(), author.getUpdatedAt());
    }

    @Test
    void testPreUpdateSetsUpdatedAt() throws InterruptedException {
        Author author = new Author();
        author.onCreate();

        LocalDateTime createdAt = author.getCreatedAt();
        Thread.sleep(10);
        author.onUpdate();

        assertEquals(createdAt, author.getCreatedAt());
        assertTrue(author.getUpdatedAt().isAfter(createdAt));
    }
}
