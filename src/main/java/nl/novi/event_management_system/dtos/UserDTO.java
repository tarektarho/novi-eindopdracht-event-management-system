package nl.novi.event_management_system.dtos;

import nl.novi.event_management_system.models.Role;
import nl.novi.event_management_system.validators.role.ValidRole;

import java.util.List;
import java.util.Set;

public class UserDTO {
    public String username;
    public String password;
    public Boolean enabled;
    public String apikey;
    public String email;

    @ValidRole
    public Set<Role> roles;

    public List<ReservationDTO> reservations;  // Mapping for reservations
    public List<FeedbackDTO> feedbacks;      // Mapping for feedbacks


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public List<ReservationDTO> getReservations() {
        return reservations;
    }

    public void setReservations(List<ReservationDTO> reservations) {
        this.reservations = reservations;
    }

    public List<FeedbackDTO> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(List<FeedbackDTO> feedbacks) {
        this.feedbacks = feedbacks;
    }
}