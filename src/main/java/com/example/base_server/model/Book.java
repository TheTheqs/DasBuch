package com.example.base_server.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-Generate
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToMany
    @JoinTable(
            name = "book_authors",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<Author> authors; //A list because a book can have more than one author.

    @Column(nullable = true)
    private String description;

    @ManyToMany
    @JoinTable(
            name = "book_keywords",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "keyword_id")
    )
    private List<KeyWord> keyWords;

    @Column
    private LocalDateTime publishedDate; //Only year

    @Column(nullable = false, unique = true)
    private String isbn;

    @Column(nullable = true)
    private String coverURL;

    @Column
    private String externalLinks;

    @ManyToMany(mappedBy = "readBooks")
    private List<User> readBy; //Stores users that already read the book

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
    public Book() {}; //Basic Constructor

    public Book(String title, List<Author> authors, String description, List<KeyWord> keyWords, LocalDateTime publishedDate, String isbn, String coverURL, String externalLinks) {
        this.title = title;
        this.authors = authors;
        this.description = description;
        this.keyWords = keyWords;
        this.publishedDate = publishedDate;
        this.isbn = isbn;
        this.coverURL = coverURL;
        this.externalLinks = externalLinks;
    }

    //Getters and Setters

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<KeyWord> getKeyWords() {
        return keyWords;
    }

    public LocalDateTime getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDateTime publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getCoverURL() {
        return coverURL;
    }

    public void setCoverURL(String coverURL) {
        this.coverURL = coverURL;
    }

    public String getExternalLinks() {
        return externalLinks;
    }

    public void setExternalLinks(String externalLinks) {
        this.externalLinks = externalLinks;
    }

    public List<User> getReadBy() {
        return readBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    //List methods
    public void addReader(User user){
        this.readBy.add(user);
    }

    public void removeReader(User user){
        this.readBy.remove(user);
    }

    public void addAuthor(Author author){
        this.authors.add(author);
    }

    public void removeAuthor(Author author){
        this.authors.remove(author);
    }

    public void addKeyWord(KeyWord keyWord){
        this.keyWords.add(keyWord);
    }

    public void removeKeyWord(KeyWord keyWord){
        this.keyWords.remove(keyWord);
    }

    //Hashcode and Equals
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id) && Objects.equals(isbn, book.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, isbn);
    }
}
