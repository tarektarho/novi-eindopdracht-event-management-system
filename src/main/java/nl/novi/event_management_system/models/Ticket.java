package nl.novi.event_management_system.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.novi.event_management_system.enums.TicketType;
import nl.novi.event_management_system.validators.ticketType.ValidTicketType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "tickets")
@Data
@NoArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "username")
    private User user;

    @ManyToOne(optional = false)  // Ensures a ticket must be linked to an event
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(nullable = false)
    @DecimalMin(value = "0.00", message = "Price must be at least 0.00")
    private BigDecimal price;

    @Column(nullable = false, unique = true, updatable = false, length = 20)
    @Size(min = 8, max = 20, message = "Ticket code must be between 8 and 20 characters")
    @Pattern(regexp = "^TICKET-[A-Z0-9]{8}$", message = "Invalid ticket code format")
    private String ticketCode = generateTicketCode();

    @Column(nullable = false, updatable = false)
    @PastOrPresent(message = "Purchase date cannot be in the future")
    private LocalDate purchaseDate = LocalDate.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ValidTicketType
    private TicketType ticketType;

    public Ticket(User user, Event event, BigDecimal price, TicketType ticketType) {
        this.user = user;
        this.event = event;
        this.price = price;
        this.ticketType = ticketType;
    }

    private String generateTicketCode() {
        return "TICKET-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
