package nl.novi.event_management_system.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@IdClass(RoleKey.class)  // Composite primary key using RoleKey
@Table(name = "roles")
public class Role implements Serializable {

    @Id
    @Column(nullable = false)
    @JsonIgnore
    private String username;  // Part of the composite key


    @Id
    @Column(nullable = false)
    private String role;


    public Role() {
    }

    public Role(String username, String role) {
        this.username = username;
        this.role = role;
    }

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

}
