package com.example.base_server.service;

import com.example.base_server.model.Author;
import com.example.base_server.repository.AuthorRepository;
import com.example.base_server.repository.BookRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthorService {
    //Dependency injection
    private final AuthorRepository authorRepository;
    private final BookService bookService;

    public AuthorService(AuthorRepository authorRepository,@Lazy BookService bookService) {
        this.authorRepository = authorRepository;
        this.bookService = bookService;
    }

    //All CRUD for authors
    //Create
    public Author createAuthor(String name) {
        return authorRepository.findByName(name)
                .orElseGet(() -> authorRepository.save(new Author(name, new HashSet<>())));
    }

    //Read
    public Author getAuthorById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Author not found"));
    }

    public Page<Author> getAuthorListByName(String name, Pageable pageable) {
        return authorRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    //Update
    @Transactional
    public Author updateAuthor(Long id, String name) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Author not found"));

        String trimmed = name.trim();
        System.out.println(trimmed);
        if (!trimmed.isEmpty() && !author.getName().equalsIgnoreCase(trimmed)) {
            //Verify if the provided new name is available.
            Optional<Author> existing = authorRepository.findByName(trimmed);
            if (existing.isPresent() && !existing.get().getId().equals(id)) {
                Author convergeAuthor = existing.get();
                bookService.transferBooks(author, convergeAuthor);
                deleteAuthor(author.getId());
                return authorRepository.save(convergeAuthor);
            }
            author.setName(trimmed);
        }
        return authorRepository.save(author);
    }

    //Delete
    public boolean deleteAuthor(Long id) {
        if (!authorRepository.existsById(id)) {
            return false;
        }
        authorRepository.deleteById(id);
        return true;
    }

}
