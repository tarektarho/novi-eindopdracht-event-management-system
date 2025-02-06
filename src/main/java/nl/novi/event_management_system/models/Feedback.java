package nl.novi.event_management_system.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table(name = "feedbacks")
public class  Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "username")
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @Column(nullable = false)
    private int rating; // 1 to 5 stars

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(nullable = false)
    private LocalDate feedbackDate = LocalDate.now();

    public Feedback() {}

    public Feedback(User user, Event event, int rating, String comment) {
        this.user = user;
        this.event = event;
        this.rating = rating;
        this.comment = comment;
        this.feedbackDate = LocalDate.now();
    }

}
