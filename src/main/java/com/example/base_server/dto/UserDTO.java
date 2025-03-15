package com.example.base_server.dto;

import com.example.base_server.enums.Role;
import com.example.base_server.model.User;

//This class is a clone for the User class, but without it sensible data.
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private Boolean isActive;

    public UserDTO(){}

    // Constructor
    public UserDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.isActive = user.getIsActive();
    }

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public Role getRole() { return role; }
    public Boolean getIsActive() { return isActive; }
}
