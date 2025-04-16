package com.example.base_server.dto;

import java.util.Objects;
import java.util.Set;

public class UpdateBookDTO {
    private String title;
    private Set<Long> authorIds;

    // Getters e Setters

    public Set<Long> getAuthorIds() {
        return authorIds;
    }

    public void setAuthorIds(Set<Long> authorIds) {
        this.authorIds = authorIds;
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
        return Objects.equals(title, that.title) && Objects.equals(authorIds, that.authorIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, authorIds);
    }
}
