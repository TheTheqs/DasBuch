package com.example.base_server.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "books", uniqueConstraints = {@UniqueConstraint(columnNames = {"title", "author"})})
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-Generate
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToMany
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<Author> authors;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Review> reviews;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    //Config Methods
    @PrePersist //This function will be called everytime before it is persisted. A Hibernate feature
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate //This function  will be called everytime an entity is updated
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Book() {
    }

    public Book(String title, Set<Author> authors, Set<Review> reviews) {
        this.title = title;
        this.authors = authors;
        this.reviews = reviews;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Set<Review> getReviews() {
        return reviews;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
    }

    //List Methods
    public void addAuthor(Author author) {
        this.authors.add(author);
    }

    public void removeAuthor(Author author) {
        this.authors.remove(author);
    }

    public void addReview(Review review) {
        this.reviews.add(review);
    }

    public void removeReview(Review review) {
        this.reviews.remove(review);
    }

    //Hash code and Equals
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id) && Objects.equals(title, book.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }
}
