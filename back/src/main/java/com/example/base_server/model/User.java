package com.example.base_server.model;
//imports
import com.example.base_server.enums.Role;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity //Entity declaration
@Table(name = "users") //Table name declaration
public class User {
    //Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-Generate
    private Long id;
    @Column(nullable = false) // Declaring this Column cannot be null
    private String name;
    @Column(nullable = false, unique = true) // Declaring this cannot be null and must be unique
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
    public User() {} // Basic Constructor

    public User(String name, String email, String password, Role role){
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.isActive = false;
    }
    //Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public String getVerificationToken() { return verificationToken; }
    public void setVerificationToken(String verificationToken) { this.verificationToken = verificationToken; }

    public LocalDateTime getTokenExpirationTime() { return tokenExpirationTime; }
    public void setTokenExpirationTime(LocalDateTime tokenExpirationTime) { this.tokenExpirationTime = tokenExpirationTime; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
