package com.example.base_server.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(
        name = "reviews",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "book_id"})
        }
)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-Generate
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column
    private String synopsys;

    @Column
    private String commentary;

    @Min(0)
    @Max(10)
    @Column(nullable = false)
    private int score;

    @Column
    private LocalDateTime readAt;

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

    public Review() {
    }

    public Review(User user, Book book, String synopsys, String commentary, int score, LocalDateTime readAt) {
        this.user = user;
        this.book = book;
        this.synopsys = synopsys;
        this.commentary = commentary;
        this.score = score;
        this.readAt = readAt;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Book getBook() {
        return book;
    }

    public String getSynopsys() {
        return synopsys;
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

    public void setUser(User user) {
        this.user = user;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setSynopsys(String synopsys) {
        this.synopsys = synopsys;
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

    //Hashcode and Equals
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return Objects.equals(id, review.id) && Objects.equals(user, review.user) && Objects.equals(book, review.book);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, book);
    }
}
