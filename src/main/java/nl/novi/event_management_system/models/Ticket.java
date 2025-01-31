package nl.novi.event_management_system.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import nl.novi.event_management_system.enums.TicketType;
import nl.novi.event_management_system.validators.ticketType.ValidTicketType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "tickets")
public class Ticket {

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
    private BigDecimal price;

    @Column(nullable = false, unique = true)
    private String ticketCode;

    @Column(nullable = false)
    private LocalDateTime purchaseDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ValidTicketType
    private TicketType ticketType;

    public Ticket() {}

    public Ticket(User user, Event event, BigDecimal price, TicketType ticketType) {
        this.user = user;
        this.event = event;
        this.price = price;
        this.ticketType = ticketType;
        this.purchaseDate = LocalDateTime.now();
        this.ticketCode = generateTicketCode();
    }

    public Ticket(User user, Optional<Event> event, BigDecimal price, TicketType ticketType) {
        this.user = user;
        this.event = event.orElse(null);
        this.price = price;
        this.ticketType = ticketType;
        this.purchaseDate = LocalDateTime.now();
        this.ticketCode = generateTicketCode();
    }

    private String generateTicketCode() {
        return "TICKET-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

}
