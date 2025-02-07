package nl.novi.event_management_system.services;

import jakarta.transaction.Transactional;
import nl.novi.event_management_system.dtos.eventDtos.*;
import nl.novi.event_management_system.dtos.userDtos.UserProfileDTO;
import nl.novi.event_management_system.exceptions.EventNotFoundException;
import nl.novi.event_management_system.exceptions.RecordNotFoundException;
import nl.novi.event_management_system.exceptions.UsernameNotFoundException;
import nl.novi.event_management_system.models.Event;
import nl.novi.event_management_system.models.Feedback;
import nl.novi.event_management_system.models.Ticket;
import nl.novi.event_management_system.models.User;
import nl.novi.event_management_system.repositories.EventRepository;
import nl.novi.event_management_system.repositories.FeedbackRepository;
import nl.novi.event_management_system.repositories.TicketRepository;
import nl.novi.event_management_system.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {
    @Mock
    private EventRepository eventRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private FeedbackRepository feedbackRepository;
    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private EventService eventService;

    private UUID eventId;
    private EventCreateDTO eventCreateDTO;
    private Event storedEvent;
    private User organizer;
    private List<Event> listOfEvents;
    private List<EventParticipantUsernameDTO> participantUsernameDTOList;
    private Event event;
    private User participant1;
    private User participant2;

    private final String updatedEventName = "Updated Event";
    private final String updatedEventLocation = "Updated Location";
    private final LocalDate updatedEventEndDate = LocalDate.parse("2025-06-16");
    private final LocalDate updatedStartDate = LocalDate.parse("2025-06-16");
    private final double updatedEventPrice = 200.00;
    private final int updatedEventCapacity = 200;

    private final String originalEventName = "Original Event";
    private final String originalEventLocation = "Original Location";
    private final LocalDate originalEventEndDate = LocalDate.parse("2023-11-02");
    private final LocalDate originalStartDate = LocalDate.parse("2023-11-01");
    private final double originalEventPrice = 25.0;
    private final int originalEventCapacity = 50;

    private final String organizerUsername = "organizer_user";

    @BeforeEach
    void setUp() {
        // Arrange
        eventId = UUID.randomUUID();
        eventCreateDTO = new EventCreateDTO();
        eventCreateDTO.setName(updatedEventName);
        eventCreateDTO.setLocation(updatedEventLocation);
        eventCreateDTO.setStartDate(updatedStartDate);
        eventCreateDTO.setEndDate(updatedEventEndDate);
        eventCreateDTO.setCapacity(updatedEventCapacity);
        eventCreateDTO.setPrice(updatedEventPrice);
        eventCreateDTO.setOrganizerUsername(organizerUsername);

        storedEvent = new Event();
        storedEvent.setId(eventId);
        storedEvent.setName(originalEventName);
        storedEvent.setLocation(originalEventLocation);
        storedEvent.setStartDate(originalStartDate);
        storedEvent.setEndDate(originalEventEndDate);
        storedEvent.setCapacity(originalEventCapacity);
        storedEvent.setPrice(originalEventPrice);

        organizer = new User();
        organizer.setUsername(organizerUsername);

        listOfEvents = List.of(storedEvent, storedEvent);

        participantUsernameDTOList = new ArrayList<>();
        participantUsernameDTOList.add(new EventParticipantUsernameDTO("user1"));
        participantUsernameDTOList.add(new EventParticipantUsernameDTO("user2"));

        event = new Event();
        event.setId(eventId);
        event.setParticipants(new ArrayList<>());

        participant1 = new User();
        participant1.setUsername("user1");

        participant2 = new User();
        participant2.setUsername("user2");
    }

    @Test
    void createEventDoesCreateEventWithCorrectData() {
        when(eventRepository.save(any(Event.class))).thenReturn(storedEvent);

        // Act
        EventResponseDTO eventResponseDTO = eventService.createEvent(eventCreateDTO);

        // Assert
        assertNotNull(eventResponseDTO);
        assertEquals(updatedEventName, eventResponseDTO.getName());
        assertEquals(updatedEventLocation, eventResponseDTO.getLocation());
        assertEquals(updatedStartDate, eventResponseDTO.getStartDate());
        assertEquals(updatedEventEndDate, eventResponseDTO.getEndDate());
        assertEquals(updatedEventCapacity, eventResponseDTO.getCapacity());
        assertEquals(updatedEventPrice, eventResponseDTO.getPrice());
    }

    @Test
    void findEventByIdDoesReturnTheCorrectEvent() {
        UUID eventId = UUID.randomUUID();
        // Arrange
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(storedEvent));

        // Act
        EventResponseDTO eventResponseDTO = eventService.findEventById(eventId);

        // Assert
        assertNotNull(eventResponseDTO);
        assertEquals(originalEventName, eventResponseDTO.getName());
        assertEquals(originalEventLocation, eventResponseDTO.getLocation());
        assertEquals(originalStartDate, eventResponseDTO.getStartDate());
        assertEquals(originalEventEndDate, eventResponseDTO.getEndDate());
        assertEquals(originalEventCapacity, eventResponseDTO.getCapacity());
        assertEquals(originalEventPrice, eventResponseDTO.getPrice());
    }

    @Test
    void getEventsByOrganizerDoesReturnTheCorrectEvents() {
        // Arrange
        when(eventRepository.findByOrganizerUsername(organizerUsername)).thenReturn(listOfEvents);

        // Act
        List<EventResponseDTO> eventResponseDTOs = eventService.getEventsByOrganizer(organizerUsername);

        // Assert
        assertNotNull(eventResponseDTOs);
        assertEquals(2, eventResponseDTOs.size());
    }

    @Test
    void getAllEventsDoesReturnAllEvents() {
        // Arrange
        when(eventRepository.findAll()).thenReturn(listOfEvents);

        // Act
        List<EventResponseDTO> eventResponseDTOs = eventService.getAllEvents();

        // Assert
        assertNotNull(eventResponseDTOs);
        assertEquals(2, eventResponseDTOs.size());
    }

    @Test
    public void testUpdateEvent_Success() {
        // Arrange
        when(eventRepository.existsById(eventId)).thenReturn(true);
        when(eventRepository.findEventById(eventId)).thenReturn(Optional.of(storedEvent));
        when(userRepository.findByUsername(eventCreateDTO.getOrganizerUsername()))
                .thenReturn(Optional.of(organizer));
        when(eventRepository.save(any(Event.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        EventResponseDTO responseDTO = eventService.updateEvent(eventId, eventCreateDTO);

        // Assert
        assertNotNull(responseDTO);
        assertEquals(eventCreateDTO.getName(), responseDTO.getName());
        assertEquals(eventCreateDTO.getLocation(), responseDTO.getLocation());
        assertEquals(eventCreateDTO.getStartDate(), responseDTO.getStartDate());
        assertEquals(eventCreateDTO.getEndDate(), responseDTO.getEndDate());
        assertEquals(eventCreateDTO.getCapacity(), responseDTO.getCapacity());
        assertEquals(eventCreateDTO.getPrice(), responseDTO.getPrice());
        assertEquals(organizer.getUsername(), responseDTO.getOrganizer().getUsername());

        verify(eventRepository, times(1)).existsById(eventId);
        verify(eventRepository, times(1)).findEventById(eventId);
        verify(userRepository, times(1)).findByUsername(eventCreateDTO.getOrganizerUsername());
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    public void testUpdateEvent_SuccessWithoutOrganizer() {
        // Arrange
        eventCreateDTO.setOrganizerUsername(null); // No organizer provided

        when(eventRepository.existsById(eventId)).thenReturn(true);
        when(eventRepository.findEventById(eventId)).thenReturn(Optional.of(storedEvent));
        when(eventRepository.save(any(Event.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        EventResponseDTO responseDTO = eventService.updateEvent(eventId, eventCreateDTO);

        // Assert
        assertNotNull(responseDTO);
        assertEquals(eventCreateDTO.getName(), responseDTO.getName());
        assertEquals(eventCreateDTO.getLocation(), responseDTO.getLocation());
        assertEquals(eventCreateDTO.getStartDate(), responseDTO.getStartDate());
        assertEquals(eventCreateDTO.getEndDate(), responseDTO.getEndDate());
        assertEquals(eventCreateDTO.getCapacity(), responseDTO.getCapacity());
        assertEquals(eventCreateDTO.getPrice(), responseDTO.getPrice());
        assertEquals(responseDTO.getOrganizer(), new UserProfileDTO());

        verify(eventRepository, times(1)).existsById(eventId);
        verify(eventRepository, times(1)).findEventById(eventId);
        verify(userRepository, never()).findByUsername(any()); // No organizer lookup
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    public void testUpdateEvent_EventNotFound() {
        // Arrange
        when(eventRepository.existsById(eventId)).thenReturn(false);

        // Act & Assert
        assertThrows(EventNotFoundException.class, () -> eventService.updateEvent(eventId, eventCreateDTO));
        verify(eventRepository, times(1)).existsById(eventId);
        verify(eventRepository, never()).findEventById(any());
        verify(userRepository, never()).findByUsername(any());
        verify(eventRepository, never()).save(any());
    }

    @Test
    public void testUpdateEvent_OrganizerNotFound() {
        // Arrange
        when(eventRepository.existsById(eventId)).thenReturn(true);
        when(eventRepository.findEventById(eventId)).thenReturn(Optional.of(storedEvent));
        when(userRepository.findByUsername(eventCreateDTO.getOrganizerUsername()))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> eventService.updateEvent(eventId, eventCreateDTO));
        verify(eventRepository, times(1)).existsById(eventId);
        verify(eventRepository, times(1)).findEventById(eventId);
        verify(userRepository, times(1)).findByUsername(eventCreateDTO.getOrganizerUsername());
        verify(eventRepository, never()).save(any());
    }

    @Test
    public void testDeleteEventById_EventFoundAndDeleted() {
        // Arrange
        when(eventRepository.findEventById(eventId)).thenReturn(Optional.of(new Event()));
        when(eventRepository.existsById(eventId)).thenReturn(true);

        // Act
        boolean result = eventService.deleteEventById(eventId);

        // Assert
        assertTrue(result);
        verify(eventRepository, times(1)).findEventById(eventId);
        verify(eventRepository, times(1)).existsById(eventId);
        verify(eventRepository, times(1)).deleteById(eventId);
    }

    @Test
    public void testDeleteEventById_EventFoundButNotDeleted() {
        // Arrange
        when(eventRepository.findEventById(eventId)).thenReturn(Optional.of(new Event()));
        when(eventRepository.existsById(eventId)).thenReturn(false);

        // Act
        boolean result = eventService.deleteEventById(eventId);

        // Assert
        assertFalse(result);
        verify(eventRepository, times(1)).findEventById(eventId);
        verify(eventRepository, times(1)).existsById(eventId);
        verify(eventRepository, never()).deleteById(any());
    }

    @Test
    public void testDeleteEventById_EventNotFound() {
        // Arrange
        when(eventRepository.findEventById(eventId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EventNotFoundException.class, () -> eventService.deleteEventById(eventId));
        verify(eventRepository, times(1)).findEventById(eventId);
        verify(eventRepository, never()).existsById(any());
        verify(eventRepository, never()).deleteById(any());
    }

    @Test
    public void testAssignParticipantToEvent_ParticipantListIsNull() {
        // Arrange
        List<EventParticipantUsernameDTO> nullList = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> eventService.assignParticipantToEvent(eventId, nullList));
        verify(eventRepository, never()).findEventById(any());
        verify(userRepository, never()).findByUsername(any());
        verify(eventRepository, never()).save(any());
    }

    @Test
    public void testAssignParticipantToEvent_ParticipantListIsEmpty() {
        // Arrange
        List<EventParticipantUsernameDTO> emptyList = Collections.emptyList();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> eventService.assignParticipantToEvent(eventId, emptyList));
        verify(eventRepository, never()).findEventById(any());
        verify(userRepository, never()).findByUsername(any());
        verify(eventRepository, never()).save(any());
    }

    @Test
    public void testAssignParticipantToEvent_ParticipantNotFound() {
        // Arrange
        when(eventRepository.findEventById(eventId)).thenReturn(Optional.of(event));
        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(participant1));
        when(userRepository.findByUsername("user2")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> eventService.assignParticipantToEvent(eventId, participantUsernameDTOList));
        verify(eventRepository, times(1)).findEventById(eventId);
        verify(userRepository, times(1)).findByUsername("user1");
        verify(userRepository, times(1)).findByUsername("user2");
        verify(eventRepository, never()).save(any());
    }

    @Test
    public void testAssignParticipantToEventThrowsEventNotFoundException() {
        // Arrange
        when(eventRepository.findEventById(eventId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EventNotFoundException.class, () -> eventService.assignParticipantToEvent(eventId, participantUsernameDTOList));
        verify(eventRepository, times(1)).findEventById(eventId);
        verify(userRepository, never()).findByUsername(any());
        verify(eventRepository, never()).save(any());
    }

    @Test
    @Transactional
    public void testAssignParticipantToEvent_Success() {
        // Arrange
        when(eventRepository.findEventById(eventId)).thenReturn(Optional.of(event));
        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(participant1));
        when(userRepository.findByUsername("user2")).thenReturn(Optional.of(participant2));
        when(eventRepository.save(event)).thenReturn(event);

        // Act
        eventService.assignParticipantToEvent(eventId, participantUsernameDTOList);

        // Assert
        assertEquals(2, event.getParticipants().size());
        assertTrue(event.getParticipants().contains(participant1));
        assertTrue(event.getParticipants().contains(participant2));

        verify(eventRepository, times(1)).findEventById(eventId);
        verify(userRepository, times(1)).findByUsername("user1");
        verify(userRepository, times(1)).findByUsername("user2");
        verify(eventRepository, times(1)).save(event);
    }

    @Test
    @Transactional
    public void testAssignParticipantToEvent_AvoidDuplicates() {
        // Arrange
        event.getParticipants().add(participant1); // Add participant1 to simulate an existing participant

        when(eventRepository.findEventById(eventId)).thenReturn(Optional.of(event));
        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(participant1));
        when(userRepository.findByUsername("user2")).thenReturn(Optional.of(participant2));
        when(eventRepository.save(event)).thenReturn(event);

        // Act
        eventService.assignParticipantToEvent(eventId, participantUsernameDTOList);

        // Assert
        assertEquals(2, event.getParticipants().size()); // Ensure no duplicates
        assertTrue(event.getParticipants().contains(participant1));
        assertTrue(event.getParticipants().contains(participant2));

        verify(eventRepository, times(1)).findEventById(eventId);
        verify(userRepository, times(1)).findByUsername("user1");
        verify(userRepository, times(1)).findByUsername("user2");
        verify(eventRepository, times(1)).save(event);
    }

    @Test
    @Transactional
    public void testRemoveParticipantFromEvent_Success() {
        // Arrange
        event.getParticipants().add(participant1);
        event.getParticipants().add(participant2);

        when(eventRepository.findEventById(eventId)).thenReturn(Optional.of(event));
        when(userRepository.findById("user1")).thenReturn(Optional.of(participant1));
        when(eventRepository.save(event)).thenReturn(event);

        // Act
        eventService.removeParticipantFromEvent(eventId, new EventParticipantUsernameDTO("user1"));

        // Assert
        assertEquals(1, event.getParticipants().size());
        assertFalse(event.getParticipants().contains(participant1));
        assertTrue(event.getParticipants().contains(participant2));

        verify(eventRepository, times(1)).findEventById(eventId);
        verify(userRepository, times(1)).findById("user1");
        verify(userRepository, never()).findById("user2");
        verify(eventRepository, times(1)).save(event);
    }

    @Test
    public void testRemoveParticipantFromEventThrowsIllegalStateException() {
        // Arrange
        when(userRepository.findById("user1")).thenReturn(Optional.of(participant1));
        when(eventRepository.findEventById(eventId)).thenReturn(Optional.of(event));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> eventService.removeParticipantFromEvent(eventId, new EventParticipantUsernameDTO("user1")));
        verify(eventRepository, times(1)).findEventById(eventId);
        verify(eventRepository, never()).save(any());
    }

    @Test
    @Transactional
    public void testAddTicketsToEventAddTicketsSuccessfully() {
        UUID ticketId_1 = UUID.randomUUID();
        UUID ticketId_2 = UUID.randomUUID();

        Ticket ticket1 = new Ticket();
        ticket1.setId(ticketId_1);

        Ticket ticket2 = new Ticket();
        ticket2.setId(ticketId_2);

        // Arrange
        when(eventRepository.findEventById(eventId)).thenReturn(Optional.of(event));
        when(ticketRepository.findById(ticketId_1)).thenReturn(Optional.of(ticket1));
        when(ticketRepository.findById(ticketId_2)).thenReturn(Optional.of(ticket2));

        List<EventTicketIdDTO> ticketIdList = new ArrayList<>();

        ticketIdList.add(new EventTicketIdDTO(ticketId_1));
        ticketIdList.add(new EventTicketIdDTO(ticketId_2));

        // Act
        eventService.addTicketsToEvent(eventId, ticketIdList);

        // Assert
        assertEquals(2, event.getTickets().size());
        verify(eventRepository, times(1)).findEventById(eventId);
        verify(eventRepository, times(1)).save(any());
    }

    @Test
    public void testAddTicketsToEventThrowsEventNotFoundException() {
        // Arrange
        when(eventRepository.findEventById(eventId)).thenReturn(Optional.empty());

        List<EventTicketIdDTO> ticketIdList = new ArrayList<>();
        ticketIdList.add(new EventTicketIdDTO(UUID.randomUUID()));

        // Act & Assert
        assertThrows(EventNotFoundException.class, () -> eventService.addTicketsToEvent(eventId, ticketIdList));
        verify(eventRepository, times(1)).findEventById(eventId);
        verify(eventRepository, never()).save(any());
    }

    @Test
    public void testAddTicketsToEventThrowsRecordNotFoundException() {
        // Arrange
        when(eventRepository.findEventById(eventId)).thenReturn(Optional.of(event));
        when(ticketRepository.findById(any())).thenReturn(Optional.empty());

        List<EventTicketIdDTO> ticketIdList = new ArrayList<>();
        ticketIdList.add(new EventTicketIdDTO(UUID.randomUUID()));

        // Act & Assert
        assertThrows(RecordNotFoundException.class, () -> eventService.addTicketsToEvent(eventId, ticketIdList));
        verify(eventRepository, times(1)).findEventById(eventId);
        verify(eventRepository, never()).save(any());
    }

    @Test
    public void testAddTicketsToEventThrowsIllegalArgumentException() {
        List<EventTicketIdDTO> ticketIdList = new ArrayList<>();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> eventService.addTicketsToEvent(eventId, ticketIdList));
        verify(eventRepository, never()).save(any());
    }

    @Test
    @Transactional
    public void testRemoveTicketsFromEventRemoveTicketsSuccessfully() {
        // Arrange
        UUID ticketId = UUID.randomUUID();
        Ticket ticket = new Ticket();
        ticket.setId(ticketId);

        List<Ticket> ticketList = new ArrayList<>();
        ticketList.add(ticket);
        event.setTickets(ticketList);

        when(eventRepository.findEventById(eventId)).thenReturn(Optional.of(event));
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));

        // Act
        eventService.removeTicketFromEvent(eventId, new EventTicketIdDTO(ticketId));

        // Assert
        verify(eventRepository, times(1)).findEventById(eventId);
        verify(eventRepository, times(1)).save(any());
    }

    @Test
    public void testRemoveTicketsThrowsEventNotFoundException() {
        // Arrange
        when(eventRepository.findEventById(eventId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EventNotFoundException.class, () -> eventService.removeTicketFromEvent(eventId, new EventTicketIdDTO(UUID.randomUUID())));
        verify(eventRepository, times(1)).findEventById(eventId);
        verify(eventRepository, never()).save(any());
    }

    @Test
    public void testRemoveTicketsThrowsRecordNotFoundException() {
        // Arrange
        when(eventRepository.findEventById(eventId)).thenReturn(Optional.of(event));
        when(ticketRepository.findById(any())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RecordNotFoundException.class, () -> eventService.removeTicketFromEvent(eventId, new EventTicketIdDTO(UUID.randomUUID())));
        verify(eventRepository, times(1)).findEventById(eventId);
        verify(eventRepository, never()).save(any());
    }

    @Test
    public void testRemoveTicketsThrowsIllegalStateException() {
        // Arrange
        when(eventRepository.findEventById(eventId)).thenReturn(Optional.of(event));
        when(ticketRepository.findById(any())).thenReturn(Optional.of(new Ticket()));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> eventService.removeTicketFromEvent(eventId, new EventTicketIdDTO(null)));
        verify(eventRepository, never()).save(any());
    }

    @Test
    @Transactional
    public void testAddFeedbacksToEventSuccessfully() {
        // Arrange
        UUID feedbackId_1 = UUID.randomUUID();
        UUID feedbackId_2 = UUID.randomUUID();
        List<EventFeedbackIdDTO> feedbackIdList = new ArrayList<>();
        feedbackIdList.add(new EventFeedbackIdDTO(feedbackId_1));
        feedbackIdList.add(new EventFeedbackIdDTO(feedbackId_2));
        when(eventRepository.findEventById(eventId)).thenReturn(Optional.of(event));
        when(feedbackRepository.findById(feedbackId_1)).thenReturn(Optional.of(new Feedback()));
        when(feedbackRepository.findById(feedbackId_2)).thenReturn(Optional.of(new Feedback()));

        // Act
        eventService.AddFeedbacksToEvent(eventId, feedbackIdList);

        // Assert
        verify(eventRepository, times(1)).findEventById(eventId);
        verify(eventRepository, times(1)).save(any());
    }

    @Test
    public void testAddFeedbacksToEventThrowsIllegalArgumentException() {
        // Arrange
        List<EventFeedbackIdDTO> feedbackIdList = new ArrayList<>();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> eventService.AddFeedbacksToEvent(eventId, feedbackIdList));
        verify(eventRepository, never()).save(any());
    }

    @Test
    public void testAddFeedbacksToEventThrowsEventNotFoundException() {
        // Arrange
        when(eventRepository.findEventById(eventId)).thenReturn(Optional.empty());
        List<EventFeedbackIdDTO> feedbackIdList = new ArrayList<>();
        feedbackIdList.add(new EventFeedbackIdDTO(UUID.randomUUID()));

        // Act & Assert
        assertThrows(EventNotFoundException.class, () -> eventService.AddFeedbacksToEvent(eventId, feedbackIdList));
        verify(eventRepository, times(1)).findEventById(eventId);
        verify(eventRepository, never()).save(any());
    }

    @Test
    public void testAddFeedbackToEventThrowsRecordNotFoundException() {
        // Arrange
        when(eventRepository.findEventById(eventId)).thenReturn(Optional.of(event));
        when(feedbackRepository.findById(any())).thenReturn(Optional.empty());

        List<EventFeedbackIdDTO> feedbackIdList = new ArrayList<>();
        feedbackIdList.add(new EventFeedbackIdDTO(UUID.randomUUID()));

        // Act & Assert
        assertThrows(RecordNotFoundException.class, () -> eventService.AddFeedbacksToEvent(eventId, feedbackIdList));
        verify(eventRepository, times(1)).findEventById(eventId);
        verify(eventRepository, never()).save(any());
    }

    @Test
    @Transactional
    public void testRemoveFeedbackFromEventRemoveEventSuccessfully() {
        // Arrange
        UUID feedbackId = UUID.randomUUID();
        Feedback feedback = new Feedback();
        feedback.setId(feedbackId);

        List<Feedback> feedbackList = new ArrayList<>();
        feedbackList.add(feedback);
        event.setFeedbacks(feedbackList);

        when(eventRepository.findEventById(eventId)).thenReturn(Optional.of(event));
        when(feedbackRepository.findById(feedbackId)).thenReturn(Optional.of(feedback));

        // Act
        eventService.removeFeedbackFromEvent(eventId, new EventFeedbackIdDTO(feedbackId));

        // Assert
        verify(eventRepository, times(1)).findEventById(eventId);
        verify(eventRepository, times(1)).save(any());
    }

    @Test
    public void testRemoveFeedbackThrowsEventNotFoundException() {
        // Arrange
        when(eventRepository.findEventById(eventId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EventNotFoundException.class, () -> eventService.removeFeedbackFromEvent(eventId, new EventFeedbackIdDTO(UUID.randomUUID())));
        verify(eventRepository, times(1)).findEventById(eventId);
        verify(eventRepository, never()).save(any());
    }

    @Test
    public void testRemoveFeedbackThrowsRecordNotFoundException() {
        // Arrange
        when(eventRepository.findEventById(eventId)).thenReturn(Optional.of(event));
        when(feedbackRepository.findById(any())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RecordNotFoundException.class, () -> eventService.removeFeedbackFromEvent(eventId, new EventFeedbackIdDTO(UUID.randomUUID())));
        verify(eventRepository, times(1)).findEventById(eventId);
        verify(eventRepository, never()).save(any());
    }

    @Test
    public void testRemoveFeedbackThrowsIllegalArgumentException() {
        // Arrange
        EventFeedbackIdDTO feedbackId = new EventFeedbackIdDTO(null);
        when(eventRepository.findEventById(eventId)).thenReturn(Optional.of(event));
        when(feedbackRepository.findById(feedbackId.getFeedbackId())).thenReturn(Optional.of(new Feedback()));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> eventService.removeFeedbackFromEvent(eventId, feedbackId));
        verify(eventRepository, never()).save(any());
    }
}