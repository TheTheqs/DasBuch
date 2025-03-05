package com.example.base_server.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="authors")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-Generate
    private Long id;

    @Column(nullable = false) // Declaring this Column cannot be null
    private String name;

    @ManyToMany(mappedBy = "authors")
    private List<Book> books;

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

    //Constructor
    public Author(){};//Basic Constructor

    public Author(String name) {
        this.name = name;
    }

    //Getters and Setters


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    //List Method

    public void addBook(Book book){
        this.books.add(book);
    }

    public void removeBook(Book book){
        this.books.remove(book);
    }

    //Hashcode and Equals
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(id, author.id) && Objects.equals(name, author.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
