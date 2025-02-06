package nl.novi.event_management_system.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "organizer_username")
    private User organizer;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private int capacity;

    @Column(nullable = false)
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

    public Event(String name, String location, LocalDate startDate, LocalDate endDate, int capacity, double price) {
        this.name = name;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.capacity = capacity;
        this.price = price;
    }
}
