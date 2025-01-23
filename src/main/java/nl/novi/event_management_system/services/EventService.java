package nl.novi.event_management_system.services;

import nl.novi.event_management_system.dtos.eventDtos.EventCreateDTO;
import nl.novi.event_management_system.dtos.eventDtos.EventResponseDTO;
import nl.novi.event_management_system.exceptions.RecordNotFoundException;
import nl.novi.event_management_system.mappers.EventMapper;
import nl.novi.event_management_system.models.Event;
import nl.novi.event_management_system.repositories.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public EventResponseDTO createEvent(EventCreateDTO eventCreateDTO) {
        Event event = EventMapper.toEntity(eventCreateDTO);
        Event savedEvent = eventRepository.save(event);
        return EventMapper.toEventResponseDTO(savedEvent);
    }

    public List<Event> getEvents() {
        List<Event> events;
        try {
            events = eventRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Could not retrieve events");
        }
        return events;
    }

    public EventResponseDTO getEventById(long id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("Event not found for id: " + id));
        return EventMapper.toEventResponseDTO(event);
    }


    public void deleteEvent(long id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("Event not found for id: " + id));
        eventRepository.delete(event);
    }

    public EventResponseDTO updateEvent(long id, EventCreateDTO eventCreateDTO) {
        Event currentEvent = eventRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("Event not found for id: " + id));
        Event updateEvent = EventMapper.toEntity(eventCreateDTO);
        updateEvent.setId(currentEvent.getId());
        Event savedEvent = eventRepository.save(updateEvent);
        return EventMapper.toEventResponseDTO(savedEvent);
    }


}
