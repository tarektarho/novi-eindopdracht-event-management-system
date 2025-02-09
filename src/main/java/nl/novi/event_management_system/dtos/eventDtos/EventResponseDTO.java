package nl.novi.event_management_system.dtos.eventDtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import nl.novi.event_management_system.dtos.userDtos.UserProfileDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventResponseDTO {
    private UUID id;
    private String organizerUsername;
    private String name;
    private String location;
    private LocalDate startDate;
    private LocalDate endDate;
    private int capacity;
    private double price;
    private UserProfileDTO organizer;
    private List<EventTicketIdDTO> ticketList;
    private List<EventFeedbackIdDTO> feedbackList;
    private List<EventParticipantUsernameDTO> participants;

    public EventResponseDTO() {
    }

    public EventResponseDTO(UUID id, String organizerUsername, String name, String location, LocalDate startDate, LocalDate endDate, int capacity, double price, UserProfileDTO organizer, List<EventTicketIdDTO> ticketList, List<EventFeedbackIdDTO> feedbackList, List<EventParticipantUsernameDTO> participants) {
        this.id = id;
        this.organizerUsername = organizerUsername;
        this.name = name;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.capacity = capacity;
        this.price = price;
        this.organizer = organizer;
        this.ticketList = ticketList;
        this.feedbackList = feedbackList;
        this.participants = participants;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getOrganizerUsername() {
        return organizerUsername;
    }

    public void setOrganizerUsername(String organizerUsername) {
        this.organizerUsername = organizerUsername;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public UserProfileDTO getOrganizer() {
        return organizer;
    }

    public void setOrganizer(UserProfileDTO organizer) {
        this.organizer = organizer;
    }

    public List<EventTicketIdDTO> getTicketList() {
        return ticketList;
    }

    public void setTicketList(List<EventTicketIdDTO> ticketList) {
        this.ticketList = ticketList;
    }

    public List<EventFeedbackIdDTO> getFeedbackList() {
        return feedbackList;
    }

    public void setFeedbackList(List<EventFeedbackIdDTO> feedbackList) {
        this.feedbackList = feedbackList;
    }

    public List<EventParticipantUsernameDTO> getParticipants() {
        return participants;
    }

    public void setParticipants(List<EventParticipantUsernameDTO> participants) {
        this.participants = participants;
    }

}
