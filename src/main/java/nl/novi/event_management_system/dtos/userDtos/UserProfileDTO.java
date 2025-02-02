package nl.novi.event_management_system.dtos.userDtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import nl.novi.event_management_system.models.Role;
import nl.novi.event_management_system.models.UserPhoto;

import java.util.HashSet;
import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)  // This will exclude null fields from the response
public class UserProfileDTO {
        private String username;
        private String email;
        private Set<Role> roles = new HashSet<>();
        private UserPhoto userPhoto;
}
