package nl.novi.event_management_system.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.*;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @Column(nullable = false, unique = true)
    @NotBlank(message = "Username cannot be blank")
    @Size(max = 100, message = "Username must not exceed 100 characters")
    private String username;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Email cannot be blank")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    @Column(nullable = false)
    private String password; // Store as a hashed value

    @OneToMany(
            targetEntity = Role.class,
            mappedBy = "username",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();

    @Column(nullable = false)
    private Boolean enabled = true; // Account activation status

    @OneToOne
    private UserPhoto userPhoto; // user can have one profile photo

    // Relationship: A user can create multiple events (if they are an organizer)
    @OneToMany(mappedBy = "organizer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Event> eventsOrganized;

    // Relationship: A user can have multiple tickets (if they are an attendee)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ticket> tickets;

    // Relationship: A user can submit multiple feedback entries
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Feedback> feedbackList;

    public User() {
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public void setPassword(String password) {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }


    public void addRole(Role role) {
        this.roles.add(role);
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
    }
}