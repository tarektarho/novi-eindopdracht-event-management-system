package nl.novi.event_management_system.integration;

import nl.novi.event_management_system.enums.TicketType;
import nl.novi.event_management_system.models.Ticket;
import nl.novi.event_management_system.repositories.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "admin", roles = {"ADMIN"}) //Define the user role based on the security configuration
public class TicketControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TicketRepository ticketRepository;

    private final BigDecimal price = new BigDecimal("100.11");
    private final TicketType ticketType = TicketType.STANDARD;
    private final LocalDate purchaseDate = LocalDate.parse("2025-06-15");

    @BeforeEach
    void setUp() {
        ticketRepository.deleteAll();

        List<Ticket> testTickets = List.of(
                new Ticket(price, ticketType, purchaseDate),
                new Ticket(price, TicketType.VIP, purchaseDate)
        );
        ticketRepository.saveAll(testTickets);
    }

    @Test
    void testCreateTicket() throws Exception {
        mockMvc.perform(post("/api/v1/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"price\": 100.11,\n" +
                                "    \"ticketType\": \"STANDARD\",\n" +
                                "    \"purchaseDate\": \"2025-06-15\"\n" +
                                "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.ticketType").value("STANDARD"));
    }

    @Test
    void testCreateTicketReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"price\": 100.11,\n" +
                                "    \"ticketType\": \"STANDARD\"\n" +
                                "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetAllTickets() throws Exception {
        mockMvc.perform(get("/api/v1/tickets")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].ticketType").value("STANDARD"))
                .andExpect(jsonPath("$[1].ticketType").value("VIP"));
    }

    @Test
    void testGetTicketById() throws Exception {
        List<Ticket> tickets = ticketRepository.findAll();
        UUID storedTicketId = tickets.get(0).getId();
        mockMvc.perform(get("/api/v1/tickets/" + storedTicketId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticketType").value("STANDARD"));
    }

    @Test
    void testUpdateTicket() throws Exception {
        List<Ticket> tickets = ticketRepository.findAll();
        UUID storedTicketId = tickets.get(0).getId();
      mockMvc.perform(put("/api/v1/tickets/" + storedTicketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"price\": 200.11,\n" +
                                "    \"ticketType\": \"VIP\",\n" +
                                "    \"purchaseDate\": \"2025-06-15\"\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticketType").value("VIP"));
    }

    @Test
    void testDeleteTicket() throws Exception {
        List<Ticket> tickets = ticketRepository.findAll();
        UUID storedTicketId = tickets.get(0).getId();
        mockMvc.perform(delete("/api/v1/tickets/" + storedTicketId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

}
