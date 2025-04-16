package com.example.base_server.service;

import com.example.base_server.model.Author;
import com.example.base_server.model.Book;
import com.example.base_server.repository.AuthorRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.List;
import java.util.NoSuchElementException;
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
    public Author createAuthor(String name){
        return authorRepository.findByName(name)
                .orElseGet(() -> authorRepository.save(new Author(name, Set.of())));
    }

    //Read
    public Author getAuthorById(Long id){
        return authorRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Author not found"));
    }

    public Author getAuthorByName(String name){
        return authorRepository.findByName(name)
                .orElseThrow(() -> new NoSuchElementException("Author not found: " + name));
    }

    public Page<Author> getAuthorListByName(String name, Pageable pageable) {
        return authorRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    //Update
    public Author updateAuthor(String name, Set<Book> books){
        Author author = authorRepository.findByName(name.trim())
                .orElseThrow(() -> new NoSuchElementException("Author not found: " + name));
        if (!name.trim().isEmpty() && !author.getName().equals(name.trim())) {
            author.setName(name.trim());
        }
        if(books != null && !books.isEmpty()) {
            books.forEach(author::addBook);
        }
        return authorRepository.save(author);
    }

    //Delete
    public boolean deleteAuthor(Author author) {
        if(!authorRepository.existsById(author.getId())) {
           return false;
        }
        authorRepository.delete(author);
        return true;
    }

}
