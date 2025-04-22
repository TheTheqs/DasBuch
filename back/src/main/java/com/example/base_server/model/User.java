package com.example.base_server.model;
//imports

import com.example.base_server.enums.Role;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity //Entity declaration
@Table(name = "users") //Table name declaration
public class User {
    //Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-Generate
    private Long id;

    @Column(nullable = false) // Declaring this Column cannot be null
    private String name;

    @Column(nullable = false, unique = true, updatable = false) // Declaring this cannot be null and must be unique
    private String email;

    @Column(nullable = false)
    private String password; //Remember, this will be stored as a HashCode ¬¬

    @Column(nullable = false)
    @Enumerated(EnumType.STRING) // Role is an Enum (ADMIN, USER, ETC.)
    private Role role;

    @Column(nullable = false)
    private Boolean isActive = false; //Will be false at creation, then true trough email verification

    @Column
    private String verificationToken; //For email verification

    @Column
    private LocalDateTime tokenExpirationTime;

    @Column
    private String resetToken; //For password reset

    @Column
    private LocalDateTime resetTokenExpiration;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Review> reviews = new HashSet<>();

    @ManyToMany(mappedBy = "readBy")
    private Set<Book> readBooks;

    @Column(nullable = false, updatable = false) //Obviously, cannot change after creation
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

    //Constructors
    public User() {
    } // Basic Constructor

    public User(String name, String email, String password, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.isActive = false;
        this.resetToken = null;
        this.resetTokenExpiration = null;
    }
    //The following constructor is meant to be used on tests only
    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.password = "123456";
        this.role = Role.USER;
        this.isActive = false;
    }

    //Getters e Setters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    public LocalDateTime getTokenExpirationTime() {
        return tokenExpirationTime;
    }

    public void setTokenExpirationTime(LocalDateTime tokenExpirationTime) {
        this.tokenExpirationTime = tokenExpirationTime;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public LocalDateTime getResetTokenExpiration() {
        return resetTokenExpiration;
    }

    public void setResetTokenExpiration(LocalDateTime resetTokenExpiration) {
        this.resetTokenExpiration = resetTokenExpiration;
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

    public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
    }

    public Set<Book> getReadBooks() {
        return readBooks;
    }

    public void setReadBooks(Set<Book> readBooks) {
        this.readBooks = readBooks;
    }

    //List methods
    public void addReview(Review review) {
        this.reviews.add(review);
    }

    public void removeReview(Review review) {
        this.reviews.remove(review);
    }

    public void addBook(Book book){
        this.readBooks.add(book);
    }

    public void removeBook(Book book){
        this.readBooks.remove(book);
    }

    //Hashcode and Equals
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}
