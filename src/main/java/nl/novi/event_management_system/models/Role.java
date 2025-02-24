package nl.novi.event_management_system.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@IdClass(RoleKey.class)  // Composite primary key using RoleKey
@Table(name = "roles")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role implements Serializable {
    @Id
    @Column(nullable = false)
    @JsonIgnore
    private String username;  // Part of the composite key

    @Id
    @Column(nullable = false)
    private String role;
}
