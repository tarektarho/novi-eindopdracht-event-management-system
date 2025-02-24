package nl.novi.event_management_system.mappers;

import nl.novi.event_management_system.dtos.userDtos.UserCreateDTO;
import nl.novi.event_management_system.dtos.userDtos.UserProfileDTO;
import nl.novi.event_management_system.dtos.userDtos.UserResponseDTO;
import nl.novi.event_management_system.models.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class UserMapper {

    /**
     * Maps a User object to a UserResponseDTO object
     *
     * @param user User
     * @return UserResponseDTO
     */
    public static UserResponseDTO toUserResponseDTO(User user) {
        if (user == null) {
            return null;
        }

        return UserResponseDTO.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .enabled(user.getEnabled())
                .roles(user.getRoles())
                .userPhoto(user.getUserPhoto())
                .tickets(user.getTickets() != null ? TicketMapper.toResponseDTOList(user.getTickets()) : null)
                .feedbackList(user.getFeedbackList() != null ? FeedbackMapper.toResponseDTOList(user.getFeedbackList()) : null)
                .eventsOrganized(user.getEventsOrganized() != null ? EventMapper.toResponseDTOList(user.getEventsOrganized()) : null)
                .build();
    }

    /**
     * Maps a list of User objects to a list of UserResponseDTO objects
     *
     * @param users List of User
     * @return List of UserResponseDTO
     */
    public static List<UserResponseDTO> toUserResponseDTOList(List<User> users) {

        return users.stream()
                .map(UserMapper::toUserResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Maps a User object to a UserProfileDTO object
     *
     * @param user User
     * @return UserProfileDTO
     */
    public static UserProfileDTO toUserProfileResponseDTO(User user) {
        if (user == null) {
            return null;
        }

        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setUsername(user.getUsername());
        userProfileDTO.setEmail(user.getEmail());
        userProfileDTO.setRoles(user.getRoles());

        if (user.getUserPhoto() != null) {
            userProfileDTO.setUserPhoto(user.getUserPhoto());
        }

        return userProfileDTO;
    }

    /**
     * Converts a UserCreateDTO to a User entity.
     *
     * @param userCreateDTO The UserCreateDTO to convert.
     * @return The corresponding User entity.
     */
    public static User toUserEntity(UserCreateDTO userCreateDTO) {
        User user = new User();
        user.setUsername(userCreateDTO.getUsername());
        user.setEmail(userCreateDTO.getEmail());
        user.setPassword(userCreateDTO.getPassword());
        user.setEnabled(userCreateDTO.getEnabled());
        return user;
    }
}