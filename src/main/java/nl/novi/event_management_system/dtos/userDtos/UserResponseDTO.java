package nl.novi.event_management_system.dtos.userDtos;

import nl.novi.event_management_system.dtos.eventDtos.EventResponseDTO;
import nl.novi.event_management_system.dtos.feedbackDtos.FeedbackResponseDTO;
import nl.novi.event_management_system.dtos.ticketDtos.TicketResponseDTO;
import nl.novi.event_management_system.models.Role;
import nl.novi.event_management_system.models.UserPhoto;

import java.util.*;

public class UserResponseDTO {
    private String username;
    private String email;
    private String password;
    private Boolean enabled;
    private Set<Role> roles = new HashSet<>();
    private UserPhoto userPhoto;
    private List<TicketResponseDTO> tickets = new ArrayList<>();
    private List<FeedbackResponseDTO> feedbackList = new ArrayList<>();
    private List<EventResponseDTO> eventsOrganized = new ArrayList<>();

    public UserResponseDTO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public UserPhoto getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(UserPhoto userPhoto) {
        this.userPhoto = userPhoto;
    }

    public List<TicketResponseDTO> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketResponseDTO> tickets) {
        this.tickets = tickets;
    }

    public List<FeedbackResponseDTO> getFeedbackList() {
        return feedbackList;
    }

    public void setFeedbackList(List<FeedbackResponseDTO> feedbackList) {
        this.feedbackList = feedbackList;
    }

    public List<EventResponseDTO> getEventsOrganized() {
        return eventsOrganized;
    }

    public void setEventsOrganized(List<EventResponseDTO> eventsOrganized) {
        this.eventsOrganized = eventsOrganized;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UserResponseDTO that = (UserResponseDTO) obj;
        return Objects.equals(username, that.username) &&
                Objects.equals(email, that.email) &&
                Objects.equals(userPhoto, that.userPhoto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, email, userPhoto);
    }

    @Override
    public String toString() {
        return "UserResponseDTO{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", userPhoto=" + userPhoto +
                '}';
    }


}
