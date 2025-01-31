package nl.novi.event_management_system.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@IdClass(RoleKey.class)  // Composite primary key using RoleKey
@Table(name = "roles")
@Getter
@Setter
public class Role implements Serializable {

    @Id
    @Column(nullable = false)
    private String username;  // Part of the composite key


    @Id
    @Column(nullable = false)
    private String role;


    public Role() {}

    public Role(String username, String role) {
        this.username = username;
        this.role = role;
    }

}
