package nl.novi.event_management_system.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.io.Serializable;

public class RoleDTO implements Serializable {

    private String username; // Represents the user's username
    @NotNull(message = "Role cannot be null.")
    @Pattern(regexp = "^ROLE_.*$", message = "The role must start with 'ROLE_', e.g., ROLE_USER.")
    private String role; // Represents the user's role (e.g., "ADMIN", "USER")

    // Default constructor
    public RoleDTO() {
    }

    // Constructor with all fields
    public RoleDTO(String username, String role) {
        this.username = username;
        this.role = role;
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "RoleDTO{" +
                "username='" + username + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
