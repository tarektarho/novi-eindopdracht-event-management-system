package nl.novi.event_management_system.services;

import nl.novi.event_management_system.dtos.ticketDtos.TicketCreateDTO;
import nl.novi.event_management_system.dtos.ticketDtos.TicketResponseDTO;
import nl.novi.event_management_system.enums.TicketType;
import nl.novi.event_management_system.exceptions.RecordNotFoundException;
import nl.novi.event_management_system.mappers.TicketMapper;
import nl.novi.event_management_system.models.Ticket;
import nl.novi.event_management_system.repositories.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {
    @Mock
    private TicketRepository ticketRepository;
    @InjectMocks
    private TicketService ticketService;
    private List<Ticket> mockTickets;
    private final BigDecimal price = new BigDecimal("100.11");
    private final TicketType ticketType = TicketType.STANDARD;
    private final LocalDate purchaseDate = LocalDate.parse("2025-06-15");
    private final String ticketCode = "TICKET-52034A9C";

    @BeforeEach
    void setUp() {
        mockTickets = Arrays.asList(
                new Ticket(price, ticketType, purchaseDate, ticketCode),
                new Ticket(price, TicketType.VIP, purchaseDate, ticketCode)
        );
    }

    @Test
    void createTicketDoesCreateTicketWithCorrectData() {
        // Arrange
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> {
            Ticket savedTicket = invocation.getArgument(0);
            savedTicket.setId(UUID.randomUUID());
            return savedTicket;
        });

        TicketCreateDTO ticketCreateDTO = new TicketCreateDTO();
        ticketCreateDTO.setTicketCode();
        ticketCreateDTO.setPrice(price);
        ticketCreateDTO.setTicketType(ticketType);
        ticketCreateDTO.setPurchaseDate(purchaseDate);

        // Act
        TicketResponseDTO result = ticketService.createTicket(ticketCreateDTO);

        // Assert
        assertNotNull(result);
        assertTrue(result.getTicketCode().startsWith("TICKET-"));

    }

    @Test
    void getTickets() {
        //arrange
        when(ticketRepository.findAll()).thenReturn(mockTickets);

        List<TicketResponseDTO> ticketResponseDTOList = TicketMapper.toResponseDTOList(mockTickets);

        //act
        List<TicketResponseDTO> result = ticketService.getTickets();

        //assert
        assertEquals(ticketResponseDTOList, result);
    }

    @Test
    void getTicketById() {
        //arrange
        when(ticketRepository.findById(mockTickets.getFirst().getId())).thenReturn(Optional.ofNullable(mockTickets.getFirst()));

        Ticket ticket = mockTickets.getFirst();
        TicketResponseDTO ticketResponseDTO = TicketMapper.toResponseDTO(ticket);

        //act
        TicketResponseDTO result = ticketService.getTicketById(ticketResponseDTO.getId());

        //assert
        assertEquals(ticketResponseDTO, result);
    }

    @Test
    void updateTicket() {
        //arrange
        Ticket ticket = mockTickets.getFirst();
        TicketCreateDTO ticketCreateDTO = new TicketCreateDTO();
        ticketCreateDTO.setTicketCode();
        ticketCreateDTO.setPrice(price);
        ticketCreateDTO.setTicketType(ticketType);
        ticketCreateDTO.setPurchaseDate(purchaseDate);

        when(ticketRepository.existsById(ticket.getId())).thenReturn(true);
        when(ticketRepository.findById(ticket.getId())).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        //act
        TicketResponseDTO result = ticketService.updateTicket(ticket.getId(), ticketCreateDTO);

        //assert
        assertEquals(ticket.getId(), result.getId());

        assertTrue(result.getTicketCode().startsWith("TICKET-"));
        assertEquals(ticket.getPrice(), result.getPrice());
        assertEquals(ticket.getTicketType(), result.getTicketType());
        assertEquals(ticket.getPurchaseDate(), result.getPurchaseDate());
    }

    @Test
    void updateTicketShouldThrowExceptionWhenTicketNotFound() {
        // Arrange
        TicketCreateDTO ticketCreateDTO = new TicketCreateDTO();
        ticketCreateDTO.setTicketCode();
        ticketCreateDTO.setPrice(price);
        ticketCreateDTO.setTicketType(ticketType);
        ticketCreateDTO.setPurchaseDate(purchaseDate);

        // Mock ticket not found
        when(ticketRepository.existsById(mockTickets.getFirst().getId())).thenReturn(false);

        // Act & Assert
        assertThrows(RecordNotFoundException.class, () -> ticketService.updateTicket(mockTickets.getFirst().getId(), ticketCreateDTO));
    }

    @Test
    void deleteTicketById() {
        //arrange
        Ticket ticket = mockTickets.getFirst();

        when(ticketRepository.findById(ticket.getId())).thenReturn(Optional.of(ticket));
        when(ticketRepository.existsById(ticket.getId())).thenReturn(true);

        //act
        boolean result = ticketService.deleteTicketById(ticket.getId());

        //assert
        assertTrue(result);
    }

    @Test
    void deleteTicketByIdShouldThrowExceptionWhenTicketNotFound() {
        // Arrange
        Ticket ticket = mockTickets.getFirst();

        // Mock ticket not found
        when(ticketRepository.findById(ticket.getId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RecordNotFoundException.class, () -> ticketService.deleteTicketById(ticket.getId()));
    }

    @Test
    void deleteTicketByIdShouldReturnFalseWhenTicketNotFound() {
        // Arrange
        Ticket ticket = mockTickets.getFirst();

        // Mock ticket not found
        when(ticketRepository.findById(ticket.getId())).thenReturn(Optional.of(ticket));

        // Act
        boolean result = ticketService.deleteTicketById(ticket.getId());

        // Assert
        assertFalse(result);
    }
}