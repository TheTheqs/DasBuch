package com.example.base_server.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="requisitions")
public class Requisition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-Generate
    private Long id;

    @Column
    private String title;

    @Column(nullable = false)
    private String author;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

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

    //Constructors
    public Requisition(){};

    public Requisition(String title, String author, User user) {
        this.title = title;
        this.author = author;
        this.user = user;
    }
    //Getters

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public User getUser() {
        return user;
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
}
