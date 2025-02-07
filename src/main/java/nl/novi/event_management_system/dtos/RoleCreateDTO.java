package nl.novi.event_management_system.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.novi.event_management_system.validators.role.ValidRole;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleCreateDTO {

    @NotBlank(message = "Role name cannot be empty.")
    @ValidRole  // Custom validation to ensure role starts with "ROLE_"
    private String role;
}
