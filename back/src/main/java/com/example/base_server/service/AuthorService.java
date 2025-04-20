package com.example.base_server.service;

import com.example.base_server.model.Author;
import com.example.base_server.repository.AuthorRepository;
import com.example.base_server.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthorService {
    //Dependency injection
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
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
    public Author updateAuthor(Long id, String name) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Author not found, id: " + id));

        String trimmed = name.trim();
        if (!trimmed.isEmpty() && !author.getName().equalsIgnoreCase(trimmed)) {
            //Verify if the provided new name is available.
            Optional<Author> existing = authorRepository.findByName(trimmed);
            if (existing.isPresent() && !existing.get().getId().equals(id)) {
                throw new IllegalArgumentException("An author with this name already exists.");
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
