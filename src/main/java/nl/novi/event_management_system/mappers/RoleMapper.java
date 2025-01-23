package nl.novi.event_management_system.mappers;

import nl.novi.event_management_system.dtos.RoleDTO;
import nl.novi.event_management_system.models.Role;

public class RoleMapper {

    public static RoleDTO fromRole(Role role) {
        RoleDTO roleDto = new RoleDTO();
        roleDto.setUsername(role.getUsername());
        roleDto.setRole(role.getRole());
        return roleDto;
    }

    public static Role toRole(RoleDTO roleDto) {
        Role role = new Role();
        role.setUsername(roleDto.getUsername());
        role.setRole(roleDto.getRole());
        return role;
    }


}
