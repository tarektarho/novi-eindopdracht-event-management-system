package nl.novi.event_management_system.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "feedbacks")
@Data
@NoArgsConstructor
public class Feedback {

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
    @NotNull(message = "Rating is mandatory")
    private int rating; // 1 to 5 stars

    @Column(columnDefinition = "TEXT")
    @NotNull(message = "Comment is mandatory")
    private String comment;

    @Column(nullable = false)
    private LocalDate feedbackDate = LocalDate.now();
}
