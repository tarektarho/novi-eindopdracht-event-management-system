package nl.novi.event_management_system.controllers;

import nl.novi.event_management_system.dtos.eventDtos.*;
import nl.novi.event_management_system.services.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EventControllerTest {

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateEvent_ValidInput() {
        // Arrange
        EventCreateDTO eventCreateDTO = new EventCreateDTO();
        EventResponseDTO eventResponseDTO = new EventResponseDTO();
        eventResponseDTO.setId(UUID.randomUUID());
        // Mock HTTP request context
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        when(eventService.createEvent(any(EventCreateDTO.class))).thenReturn(eventResponseDTO);

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        // Act
        ResponseEntity<Object> response = eventController.createEvent(eventCreateDTO, bindingResult);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(eventResponseDTO, response.getBody());
        assertNotNull(response.getHeaders().getLocation());
    }

    @Test
    void testCreateEvent_InvalidInput() {
        // Arrange
        EventCreateDTO eventCreateDTO = new EventCreateDTO();
        BindingResult bindingResult = mock(BindingResult.class);

        when(bindingResult.hasErrors()).thenReturn(true);

        // Correct the return type to List<ObjectError>
        List<ObjectError> errors = Collections.singletonList(new ObjectError("field", "error message"));
        when(bindingResult.getAllErrors()).thenReturn(errors);

        // Act
        ResponseEntity<Object> response = eventController.createEvent(eventCreateDTO, bindingResult);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof List);
        assertEquals(1, ((List<?>) response.getBody()).size());
    }

    @Test
    void testGetEventById_Success() {
        // Arrange
        UUID eventId = UUID.randomUUID();
        EventResponseDTO eventResponseDTO = new EventResponseDTO();
        eventResponseDTO.setId(eventId);

        when(eventService.findEventById(eventId)).thenReturn(eventResponseDTO);

        // Act
        ResponseEntity<EventResponseDTO> response = eventController.getEventById(eventId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(eventResponseDTO, response.getBody());
    }

    @Test
    void testGetEventsByOrganizer_Success() {
        // Arrange
        String username = "testOrganizer";
        List<EventResponseDTO> eventList = Collections.singletonList(new EventResponseDTO());

        when(eventService.getEventsByOrganizer(username)).thenReturn(eventList);

        // Act
        ResponseEntity<List<EventResponseDTO>> response = eventController.getEventsByOrganizer(username);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(eventList, response.getBody());
    }

    @Test
    void testGetAllEvents_Success() {
        // Arrange
        List<EventResponseDTO> eventList = Collections.singletonList(new EventResponseDTO());

        when(eventService.getAllEvents()).thenReturn(eventList);

        // Act
        ResponseEntity<List<EventResponseDTO>> response = eventController.getAllEvents();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(eventList, response.getBody());
    }

    @Test
    void testUpdateEvent_Success() {
        // Arrange
        UUID eventId = UUID.randomUUID();
        EventCreateDTO eventCreateDTO = new EventCreateDTO();
        EventResponseDTO updatedEvent = new EventResponseDTO();
        updatedEvent.setId(eventId);

        when(eventService.updateEvent(eventId, eventCreateDTO)).thenReturn(updatedEvent);

        // Act
        ResponseEntity<EventResponseDTO> response = eventController.updateEvent(eventId, eventCreateDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedEvent, response.getBody());
    }

    @Test
    void testDeleteEvent_Success() {
        // Arrange
        UUID eventId = UUID.randomUUID();
        when(eventService.deleteEventById(eventId)).thenReturn(true);

        // Act
        ResponseEntity<Void> response = eventController.deleteEvent(eventId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testDeleteEvent_NotFound() {
        // Arrange
        UUID eventId = UUID.randomUUID();
        when(eventService.deleteEventById(eventId)).thenReturn(false);

        // Act
        ResponseEntity<Void> response = eventController.deleteEvent(eventId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testAssignOrganizerToEvent_Success() {
        // Arrange
        UUID eventId = UUID.randomUUID();
        EventOrganizerUsernameDTO organizerUsername = new EventOrganizerUsernameDTO();
        organizerUsername.setUsername("testOrganizer");

        // Act
        ResponseEntity<String> response = eventController.assignOrganizerToEvent(eventId, organizerUsername);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(eventService, times(1)).assignOrganizerToEvent(eventId, organizerUsername.getUsername());
    }

    @Test
    void testRemoveOrganizerFromEvent_Success() {
        // Arrange
        UUID eventId = UUID.randomUUID();
        EventOrganizerUsernameDTO organizerUsername = new EventOrganizerUsernameDTO();
        organizerUsername.setUsername("testOrganizer");

        // Act
        ResponseEntity<String> response = eventController.removeOrganizerFromEvent(eventId, organizerUsername);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(eventService, times(1)).removeOrganizerFromEvent(eventId, organizerUsername.getUsername());
    }

    @Test
    void testAssignParticipantToEvent_Success() {
        // Arrange
        UUID eventId = UUID.randomUUID();
        EventParticipantUsernameWrapperDTO wrapper = new EventParticipantUsernameWrapperDTO();
        wrapper.setParticipants(Collections.singletonList(new EventParticipantUsernameDTO("testParticipant")));

        // Act
        ResponseEntity<String> response = eventController.assignParticipantToEvent(eventId, wrapper);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(eventService, times(1)).assignParticipantToEvent(eventId, wrapper.getParticipants());
    }

    @Test
    void testRemoveParticipantsFromEvent_Success() {
        // Arrange
        UUID eventId = UUID.randomUUID();
        EventParticipantUsernameWrapperDTO wrapper = new EventParticipantUsernameWrapperDTO();
        wrapper.setParticipants(Collections.singletonList(new EventParticipantUsernameDTO("testParticipant")));

        // Act
        ResponseEntity<Map<String, Object>> response = eventController.removeParticipantsFromEvent(eventId, wrapper);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("removed"));
        EventParticipantUsernameDTO participant = wrapper.getParticipants().get(0);
        verify(eventService, times(1)).removeParticipantFromEvent(eventId, participant);
    }

    @Test
    void testAddTicketsToEvent_Success() {
        // Arrange
        UUID eventId = UUID.randomUUID();
        EventTicketIdsWrapperDTO wrapper = new EventTicketIdsWrapperDTO();
        wrapper.setTicketIds(Collections.singletonList(new EventTicketIdDTO(UUID.randomUUID())));

        // Act
        ResponseEntity<?> response = eventController.addTicketsToEvent(eventId, wrapper);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(eventService, times(1)).addTicketsToEvent(eventId, wrapper.getTicketIds());
    }

    @Test
    void testRemoveTicketsFromEvent_Success() {
        // Arrange
        UUID eventId = UUID.randomUUID();
        EventTicketIdsWrapperDTO wrapper = new EventTicketIdsWrapperDTO();
        wrapper.setTicketIds(Collections.singletonList(new EventTicketIdDTO(UUID.randomUUID())));

        // Act
        ResponseEntity<Map<String, Object>> response = eventController.removeTicketsFromEvent(eventId, wrapper);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("removed"));
        verify(eventService, times(1)).removeTicketFromEvent(eventId, wrapper.getTicketIds().get(0));
    }

    @Test
    void testAddFeedbackToEvent_Success() {
        // Arrange
        UUID eventId = UUID.randomUUID();
        EventFeedbackIdWrapperDTO wrapper = new EventFeedbackIdWrapperDTO();
        wrapper.setFeedbackIds(Collections.singletonList(new EventFeedbackIdDTO(UUID.randomUUID())));

        // Act
        ResponseEntity<?> response = eventController.addFeedbackToEvent(eventId, wrapper);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(eventService, times(1)).AddFeedbacksToEvent(eventId, wrapper.getFeedbackIds());
    }

    @Test
    void testRemoveFeedbackFromEvent_Success() {
        // Arrange
        UUID eventId = UUID.randomUUID();
        EventFeedbackIdWrapperDTO wrapper = new EventFeedbackIdWrapperDTO();
        wrapper.setFeedbackIds(Collections.singletonList(new EventFeedbackIdDTO(UUID.randomUUID())));

        // Act
        ResponseEntity<Map<String, Object>> response = eventController.removeFeedbackFromEvent(eventId, wrapper);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("removed"));
        verify(eventService, times(1)).removeFeedbackFromEvent(eventId, wrapper.getFeedbackIds().get(0));
    }
}