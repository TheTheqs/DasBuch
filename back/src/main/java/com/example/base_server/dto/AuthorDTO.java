package com.example.base_server.dto;

import com.example.base_server.model.Author;
import com.example.base_server.model.Book;

import java.util.Set;
import java.util.stream.Collectors;

public class AuthorDTO {
    private final Long id;
    private final String name;
    private final Set<String> books;

    public AuthorDTO(Author author) {
        this.id = author.getId();
        this.name = author.getName();
        this.books = author.getBooks().stream().map(Book::getTitle).collect(Collectors.toSet());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<String> getBooks() {
        return books;
    }

    @Override
    public String toString() {
        return "AuthorDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", books=" + books +
                '}';
    }
}
