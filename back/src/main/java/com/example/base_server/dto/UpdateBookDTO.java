package com.example.base_server.dto;

import java.util.Objects;
import java.util.Set;

public class UpdateBookDTO {
    private String title;
    private Set<String> authors;

    // Getters e Setters

    public Set<String> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<String> authors) {
        this.authors = authors;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UpdateBookDTO that = (UpdateBookDTO) o;
        return Objects.equals(title, that.title) && Objects.equals(authors, that.authors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, authors);
    }
}
