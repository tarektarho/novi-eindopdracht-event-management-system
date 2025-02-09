package nl.novi.event_management_system.dtos.ticketDtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import nl.novi.event_management_system.enums.TicketType;
import nl.novi.event_management_system.validators.ticketType.ValidTicketType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class TicketCreateDTO {
    @NotNull(message = "Price cannot be empty.")
    private BigDecimal price;
    @Schema(hidden = true)
    private String ticketCode;
    @NotNull(message = "purchaseDate cannot be empty.")
    private LocalDate purchaseDate;
    @Enumerated(EnumType.STRING)
    @ValidTicketType
    private TicketType ticketType;

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getTicketCode() {
        return ticketCode;
    }

    public void setTicketCode() {
        this.ticketCode = generateTicketCode();
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }

    private String generateTicketCode() {
        return "TICKET-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}