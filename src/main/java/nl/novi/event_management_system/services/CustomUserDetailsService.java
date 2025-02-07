package nl.novi.event_management_system.services;

import nl.novi.event_management_system.dtos.userDtos.UserResponseDTO;
import nl.novi.event_management_system.exceptions.UsernameNotFoundException;
import nl.novi.event_management_system.models.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserResponseDTO userResponseDTO = userService.getUserByUsername(username);

        if (userResponseDTO == null) {
            throw new UsernameNotFoundException("User with username '" + username + "' not found.");
        }

        String password = userResponseDTO.getPassword();

        Set<Role> roles = userResponseDTO.getRoles();

        if (roles == null) {
            roles = new HashSet<>();  // Ensure roles is not null
        }
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (Role role: roles) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getRole()));
        }

        boolean isEnabled = userResponseDTO.getEnabled() != null ? userResponseDTO.getEnabled() : false;


        return new org.springframework.security.core.userdetails.User(username, password, isEnabled,
                true, true, true, grantedAuthorities);
    }
}
