package nl.novi.event_management_system.mappers;

import nl.novi.event_management_system.dtos.FeedbackDTO;
import nl.novi.event_management_system.dtos.ReservationDTO;
import nl.novi.event_management_system.dtos.RoleDTO;
import nl.novi.event_management_system.dtos.UserDTO;
import nl.novi.event_management_system.models.Feedback;
import nl.novi.event_management_system.models.Reservation;
import nl.novi.event_management_system.models.Role;
import nl.novi.event_management_system.models.User;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;



public class UserMapper {

    /**
     * Converts a UserDTO to a User entity.
     */
    public static User toUser(UserDTO userDto) {
        User user = new User();

        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setEnabled(userDto.getEnabled());
        user.setApikey(userDto.getApikey());
        user.setEmail(userDto.getEmail());

        // Map roles
        if (userDto.getRoles() != null) {
            Set<Role> roles = userDto.getRoles().stream()
                    .map(roleDto -> new Role(userDto.getUsername(), roleDto.getRole()))
                    .collect(Collectors.toSet());
            user.setRoles(roles);
        }

        // Map reservations if present
        if (userDto.getReservations() != null) {
            List<Reservation> reservations = userDto.getReservations().stream()
                    .map(reservationDto -> {
                        Reservation reservation = new Reservation();
                        reservation.setId(reservationDto.getId());
                        reservation.setEvent(reservationDto.getEvent());
                        reservation.setUser(user); // Set the back-reference
                        return reservation;
                    })
                    .collect(Collectors.toList());
            user.setReservations(reservations);
        }

        // Map feedbacks if present
        if (userDto.getFeedbacks() != null) {
            List<Feedback> feedbacks = userDto.getFeedbacks().stream()
                    .map(feedbackDto -> {
                        Feedback feedback = new Feedback();
                        feedback.setId(feedbackDto.getId());
                        feedback.setComment(feedbackDto.getComment());
                        feedback.setUser(user); // Set the back-reference
                        return feedback;
                    })
                    .collect(Collectors.toList());
            user.setFeedbacks(feedbacks);
        }

        return user;
    }


    /**
     * Converts a User entity to a UserDTO.
     */
    public static UserDTO fromUser(User user){

        UserDTO dto = new UserDTO();

        dto.username = user.getUsername();
        dto.password = user.getPassword();
        dto.enabled = user.isEnabled();
        dto.apikey = user.getApikey();
        dto.email = user.getEmail();
        // Map roles
        if (user.getRoles() != null) {
            dto.setRoles(user.getRoles().stream()
                    .map(role -> new Role(role.getUsername(), role.getRole()))
                    .collect(Collectors.toSet()));
        }

        // Map reservations
        if (user.getReservations() != null) {
            dto.setReservations(user.getReservations().stream()
                    .map(reservation -> {
                        var reservationDto = new ReservationDTO();
                        reservationDto.setId(reservation.getId());
                        reservationDto.setEvent(reservation.getEvent());
                        return reservationDto;
                    })
                    .collect(Collectors.toList()));
        }

        // Map feedbacks
        if (user.getFeedbacks() != null) {
            dto.setFeedbacks(user.getFeedbacks().stream()
                    .map(feedback -> {
                        var feedbackDto = new FeedbackDTO();
                        feedbackDto.setId(feedback.getId());
                        feedbackDto.setComment(feedback.getComment());
                        return feedbackDto;
                    })
                    .collect(Collectors.toList()));
        }

        return dto;
    }

}
