package nl.novi.event_management_system.models;

import jakarta.persistence.*;
import nl.novi.event_management_system.enums.TicketType;
import nl.novi.event_management_system.validators.ticketType.ValidTicketType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

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
    private LocalDate purchaseDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ValidTicketType
    private TicketType ticketType;

    public Ticket() {
    }

    public Ticket(User user, Event event, BigDecimal price, TicketType ticketType) {
        this.user = user;
        this.event = event;
        this.price = price;
        this.ticketType = ticketType;
        this.purchaseDate = LocalDate.now();
        this.ticketCode = generateTicketCode();
    }

    public Ticket(User user, Optional<Event> event, BigDecimal price, TicketType ticketType) {
        this.user = user;
        this.event = event.orElse(null);
        this.price = price;
        this.ticketType = ticketType;
        this.purchaseDate = LocalDate.now();
        this.ticketCode = generateTicketCode();
    }

    public Ticket(BigDecimal price, TicketType ticketType, LocalDate purchaseDate, String ticketCode) {
        this.price = price;
        this.ticketType = ticketType;
        this.purchaseDate = purchaseDate;
        this.ticketCode = ticketCode;
    }

    public Ticket(BigDecimal price, TicketType ticketType, LocalDate purchaseDate) {
        this.price = price;
        this.ticketType = ticketType;
        this.purchaseDate = purchaseDate;
        this.ticketCode = generateTicketCode();
    }

    public UUID getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Event getEvent() {
        return event;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getTicketCode() {
        return ticketCode;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }

    private String generateTicketCode() {
        return "TICKET-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

}
