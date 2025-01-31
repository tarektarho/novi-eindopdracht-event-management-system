package nl.novi.event_management_system.dtos.userDtos;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import nl.novi.event_management_system.models.Role;

import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
public class UserCreateDTO {

    @NotEmpty(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;

    @NotEmpty(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotEmpty(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    private Boolean enabled;

    private Set<Role> roles = new HashSet<>();  // Assuming RoleEnum is an enum type
}
