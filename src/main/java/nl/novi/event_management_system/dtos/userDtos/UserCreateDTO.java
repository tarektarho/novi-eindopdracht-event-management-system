package nl.novi.event_management_system.dtos.userDtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import nl.novi.event_management_system.models.Role;

import java.util.HashSet;
import java.util.Set;

@Data
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

    @NotNull(message = "Enabled status is mandatory")
    private Boolean enabled;

    @NotNull(message = "Roles are mandatory")
    private Set<Role> roles = new HashSet<>();
}
