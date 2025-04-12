package com.example.base_server.dto;

import com.example.base_server.model.Book;

import java.util.Set;
import java.util.stream.Collectors;

public class BookDTO {
    private final Long id;
    private final String title;
    private final Set<AuthorDTO> authors;
    private final int reviewCount;

    public BookDTO(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.authors = book.getAuthors().stream()
                .map(AuthorDTO::new)
                .collect(Collectors.toSet());
        this.reviewCount = book.getReviews().size();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Set<AuthorDTO> getAuthors() {
        return authors;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    @Override
    public String toString() {
        return "BookDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", authors=" + authors +
                ", reviewCount=" + reviewCount +
                '}';
    }
}
