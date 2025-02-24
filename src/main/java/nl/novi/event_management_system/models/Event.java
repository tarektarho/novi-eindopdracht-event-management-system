package nl.novi.event_management_system.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "organizer_username")
    private User organizer;

    @Column(nullable = false)
    @NotNull(message = "Name cannot be empty")
    private String name;

    @Column(nullable = false)
    @NotNull(message = "Location cannot be empty")
    private String location;

    @Column(nullable = false)
    @NotNull(message = "Start date cannot be empty")
    private LocalDate startDate;

    @Column(nullable = false)
    @NotNull(message = "End date cannot be empty")
    private LocalDate endDate;

    @Column(nullable = false)
    @NotNull(message = "Capacity cannot be empty")
    private int capacity;

    @Column(nullable = false)
    @NotNull(message = "Price cannot be empty")
    private double price;

    @ManyToMany
    @JoinTable(
            name = "event_tickets",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "ticket_id")
    )
    private List<Ticket> tickets = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "event_feedback",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "feedback_id")
    )
    private List<Feedback> feedbacks = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "event_participants",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "username")
    )
    private List<User> participants = new ArrayList<>();
}
