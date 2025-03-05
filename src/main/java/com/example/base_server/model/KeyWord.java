package com.example.base_server.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="keywords")
public class KeyWord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-Generate
    private Long id;
    @Column(nullable = false, unique = true)
    private String value;

    @ManyToMany(mappedBy = "keyWords")
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
    public KeyWord() {}; //Basic constructor

    public KeyWord(String value) {
        this.value = value;
    }

    //Getters and Setters


    public Long getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public List<Book> getBooks() {
        return books;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    //List Methods

    public void addBook(Book book){
        this.books.add(book);
    }

    public void removeBook(Book book){
        this.books.remove(book);
    }

    //Hashcode adn Equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeyWord keyWord = (KeyWord) o;
        return Objects.equals(id, keyWord.id) && Objects.equals(value, keyWord.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value);
    }
}
