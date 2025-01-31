package nl.novi.event_management_system.dtos.userDtos;

import lombok.Getter;
import lombok.Setter;
import nl.novi.event_management_system.models.Role;
import nl.novi.event_management_system.models.UserPhoto;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
public class UserResponseDTO {

    // Getters and Setters
    private String username;
    private String email;
    private String password;
    private Boolean enabled;
    private Set<Role> roles = new HashSet<>();
    private UserPhoto userPhoto;


}
