package nl.novi.event_management_system.integration;

import nl.novi.event_management_system.enums.TicketType;
import nl.novi.event_management_system.models.Event;
import nl.novi.event_management_system.models.Ticket;
import nl.novi.event_management_system.models.User;
import nl.novi.event_management_system.repositories.EventRepository;
import nl.novi.event_management_system.repositories.TicketRepository;
import nl.novi.event_management_system.repositories.UserRepository;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "admin", roles = {"ADMIN"}) // Define the user role based on the security configuration
public class TicketControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    private final BigDecimal price = new BigDecimal("100.11");
    private final TicketType ticketType = TicketType.STANDARD;
    private final LocalDate purchaseDate = LocalDate.now();

    private User user;
    private Event event;

    @BeforeEach
    void setUp() {
        // Clear database before each test
        ticketRepository.deleteAll();
        userRepository.deleteAll();
        eventRepository.deleteAll();

        // Create and save User
        user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("securepassword");
        user = userRepository.save(user); // Persist user

        // Create and save Event
        event = new Event();
        event.setId(UUID.randomUUID()); // Generate a new ID
        event.setName("Test Event");
        event.setStartDate(LocalDate.now());
        event.setEndDate(LocalDate.now().plusDays(1));
        event.setLocation("Test Location");
        event = eventRepository.save(event); // Persist event

        // Create and save Tickets after persisting User and Event
        List<Ticket> testTickets = List.of(
                new Ticket(user, event, price, ticketType, purchaseDate),
                new Ticket(user, event, price, TicketType.VIP, purchaseDate)
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
                                "    \"purchaseDate\": \"2024-06-15\",\n" +
                                "    \"username\": \"" + user.getUsername() + "\",\n" +
                                "    \"eventId\": \"" + event.getId() + "\"\n" +
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
        UUID storedTicketId = tickets.getFirst().getId();
        mockMvc.perform(get("/api/v1/tickets/" + storedTicketId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticketType").value("STANDARD"));
    }

    @Test
    void testUpdateTicket() throws Exception {
        // Ensure tickets exist before fetching
        List<Ticket> tickets = ticketRepository.findAll();
        assertFalse(tickets.isEmpty(), "No tickets found in the repository.");
        LocalDate purchaseDate = LocalDate.now();
        UUID storedTicketId = tickets.getFirst().getId();

        mockMvc.perform(put("/api/v1/tickets/" + storedTicketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"price\": 200.11,\n" +
                                "    \"ticketType\": \"VIP\",\n" +
                                "    \"purchaseDate\": \"" + purchaseDate + "\",\n" +
                                "    \"username\": \"" + user.getUsername() + "\",\n" +
                                "    \"eventId\": \"" + event.getId() + "\"\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticketType").value("VIP"))
                .andExpect(jsonPath("$.price").value(200.11));
    }

    @Test
    void testUpdateTicketWithoutUser() throws Exception {
        List<Ticket> tickets = ticketRepository.findAll();
        LocalDate purchaseDate = LocalDate.now();
        assertFalse(tickets.isEmpty(), "No tickets found in the repository.");

        UUID storedTicketId = tickets.get(0).getId();

        mockMvc.perform(put("/api/v1/tickets/" + storedTicketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"price\": 200.11,\n" +
                                "    \"ticketType\": \"VIP\",\n" +
                                "    \"purchaseDate\": \"" + purchaseDate + "\",\n" +
                                "    \"eventId\": \"" + event.getId() + "\"\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticketType").value("VIP"))
                .andExpect(jsonPath("$.price").value(200.11));
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
