package nl.novi.event_management_system.dtos;

import jakarta.validation.constraints.NotBlank;
import nl.novi.event_management_system.validators.role.ValidRole;

public class RoleCreateDTO {

    @NotBlank(message = "Role name cannot be empty.")
    @ValidRole  // Custom validation to ensure role starts with "ROLE_"
    private String role;

    public RoleCreateDTO() {
    }

    public RoleCreateDTO(String role) {
        this.role = role;
    }


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
