package com.example.base_server.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Check;

import java.time.LocalDateTime;

//The main clas from the project
@Entity
@Table(name="reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-Generate
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column
    private String commentary;

    @Check(constraints = "score >= 1 AND score <= 10")
    @Column(nullable = false)
    private int score;

    public void setUser(User user) {
        this.user = user;
    }

    @Column
    private LocalDateTime readAt;

    @Column
    private LocalDateTime createdAt;

    @Column
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

    public Review() {}

    public Review(Book book, User user, String commentary, int score, LocalDateTime readAt) {
        this.book = book;
        this.user = user;
        this.commentary = commentary;
        this.score = score;
        this.readAt = readAt;
    }

    public Long getId() {
        return id;
    }

    public Book getBook() {
        return book;
    }

    public User getUser() {
        return user;
    }

    public String getCommentary() {
        return commentary;
    }

    public int getScore() {
        return score;
    }

    public LocalDateTime getReadAt() {
        return readAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setReadAt(LocalDateTime readAt) {
        this.readAt = readAt;
    }
}
