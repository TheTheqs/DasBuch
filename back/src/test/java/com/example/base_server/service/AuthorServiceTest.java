package com.example.base_server.service;

import com.example.base_server.model.Author;
import com.example.base_server.repository.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class AuthorServiceTest {

    @Autowired
    private AuthorService authorService;

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    void shouldCreateNewAuthorIfNotExists() {
        // Tenta criar um autor que ainda não existe
        Author author = authorService.createAuthor("Stephen King");

        assertNotNull(author.getId());
        assertEquals("Stephen King", author.getName());
    }

    @Test
    void shouldReturnExistingAuthorIfAlreadyExists() {
        // Cria manualmente o autor
        Author existing = authorRepository.save(new Author("Stephen King", null));

        // Chama o service com o mesmo nome
        Author result = authorService.createAuthor("Stephen King");

        // Deve retornar o mesmo autor (sem duplicar)
        assertEquals(existing.getId(), result.getId());
        assertEquals(1, authorRepository.findAll().size());
    }

    @Test
    void shouldReturnAuthorByIdIfExists() {
        Author saved = authorRepository.save(new Author("Clarice Lispector", null));

        Author result = authorService.getAuthorById(saved.getId());

        assertEquals(saved.getName(), result.getName());
    }

    @Test
    void shouldThrowExceptionIfAuthorNotFoundById() {
        assertThrows(NoSuchElementException.class, () -> {
            authorService.getAuthorById(9999L);
        });
    }

    @Test
    void shouldSearchAuthorsByNameContainingIgnoreCase() {
        authorRepository.save(new Author("Stephen King", null));
        authorRepository.save(new Author("Stephen Edwin King", null));
        authorRepository.save(new Author("J.K. Rowling", null));

        Page<Author> page = authorService.getAuthorListByName("king", PageRequest.of(0, 10));

        assertEquals(2, page.getContent().size());
        assertTrue(page.getContent().stream().allMatch(a -> a.getName().toLowerCase().contains("king")));
    }

    @Test
    void shouldUpdateAuthorNameIfValidAndDifferent() {
        Author saved = authorRepository.save(new Author("King", null));

        Author updated = authorService.updateAuthor(saved.getId(), "Stephen King");

        assertEquals("Stephen King", updated.getName());
    }

    @Test
    void shouldNotUpdateIfNameIsEmptyOrSame() {
        Author saved = authorRepository.save(new Author("King", null));

        // Mesmo nome
        Author same = authorService.updateAuthor(saved.getId(), "King");
        assertEquals("King", same.getName());

        // Nome vazio
        Author empty = authorService.updateAuthor(saved.getId(), "   ");
        assertEquals("King", empty.getName());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonexistentAuthor() {
        assertThrows(NoSuchElementException.class, () -> {
            authorService.updateAuthor(9999L, "Nome Qualquer");
        });
    }

    @Test
    void shouldNotAllowUpdatingToExistingAuthorName() {
        // Cria dois autores diferentes
        Author a1 = authorRepository.save(new Author("George Orwell", null));
        Author a2 = authorRepository.save(new Author("Eric Arthur Blair", null));

        // Tenta mudar o nome de a2 para o mesmo de a1
        assertThrows(IllegalArgumentException.class, () -> {
            authorService.updateAuthor(a2.getId(), "George Orwell");
        });
    }

    @Test
    void shouldDeleteAuthorIfExists() {
        Author saved = authorRepository.save(new Author("Carlos Drummond", null));

        boolean result = authorService.deleteAuthor(saved.getId());

        assertTrue(result);
        assertFalse(authorRepository.existsById(saved.getId()));
    }

    @Test
    void shouldReturnFalseIfAuthorToDeleteDoesNotExist() {
        boolean result = authorService.deleteAuthor(9999L);

        assertFalse(result);
    }
}

