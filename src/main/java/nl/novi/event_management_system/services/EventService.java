package nl.novi.event_management_system.services;

import jakarta.validation.Valid;
import nl.novi.event_management_system.dtos.eventDtos.EventCreateDTO;
import nl.novi.event_management_system.dtos.eventDtos.EventResponseDTO;
import nl.novi.event_management_system.dtos.userDtos.UserResponseDTO;
import nl.novi.event_management_system.exceptions.EventNotFoundException;
import nl.novi.event_management_system.exceptions.UsernameNotFoundException;
import nl.novi.event_management_system.mappers.EventMapper;
import nl.novi.event_management_system.models.Event;
import nl.novi.event_management_system.models.User;
import nl.novi.event_management_system.repositories.EventRepository;
import nl.novi.event_management_system.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserRepository userRepository;


    public EventResponseDTO createEvent(@Valid EventCreateDTO eventCreateDTO) {
        Event event = EventMapper.toEntity(eventCreateDTO);
        eventRepository.save(event);

        return EventMapper.toResponseDTO(event);
    }

    public EventResponseDTO findEventById(UUID id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException(id));

        return EventMapper.toResponseDTO(event);
    }

    public List<EventResponseDTO> getEventsByOrganizer(String username) {

        return EventMapper.toResponseDTOList(eventRepository.findByOrganizerUsername(username));
    }

    public List<EventResponseDTO> getAllEvents() {
        return EventMapper.toResponseDTOList(eventRepository.findAll());
    }

    public EventResponseDTO updateEvent(UUID id, EventCreateDTO eventCreateDTO) {
        if (!eventRepository.existsById(id)) throw new EventNotFoundException(id);
        Event storedEvent = eventRepository.findEventById(id).orElseThrow(() -> new EventNotFoundException(id));
        Event updatedEvent = EventMapper.toEntity(eventCreateDTO);
        updatedEvent.setId(storedEvent.getId());
        User user = new User();
        if (eventCreateDTO.getOrganizerUsername() != null) {
            user = userRepository.findByUsername(eventCreateDTO.getOrganizerUsername())
                    .orElseThrow(() -> new UsernameNotFoundException(eventCreateDTO.getOrganizerUsername()));
        }
        updatedEvent.setOrganizer(user);
        Event savedEvent = eventRepository.save(updatedEvent);

        return EventMapper.toResponseDTO(savedEvent);

    }

    public boolean deleteEventById(UUID id) {
        eventRepository.findEventById(id)
                .orElseThrow(() -> new EventNotFoundException(id));

        if (eventRepository.existsById(id)) {
            eventRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
