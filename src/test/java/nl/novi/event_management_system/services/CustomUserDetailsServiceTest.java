package nl.novi.event_management_system.services;

import nl.novi.event_management_system.dtos.userDtos.UserResponseDTO;
import nl.novi.event_management_system.enums.RoleEnum;
import nl.novi.event_management_system.exceptions.UsernameNotFoundException;
import nl.novi.event_management_system.models.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashSet;
import java.util.Set;

import static nl.novi.event_management_system.enums.RoleEnum.getRoleName;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private UserResponseDTO userResponseDTO;

    private String username = "testuser";
    private String adminRole = getRoleName(RoleEnum.ADMIN);
    private String userRole = getRoleName(RoleEnum.PARTICIPANT);

    @BeforeEach
    public void setUp() {
        // Initialize a UserResponseDTO with roles and enabled status
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(username, adminRole));
        roles.add(new Role(username, userRole));

        userResponseDTO = new UserResponseDTO();
        userResponseDTO.setUsername(username);
        userResponseDTO.setPassword("password");
        userResponseDTO.setRoles(roles);
        userResponseDTO.setEnabled(true);
    }

    @Test
    public void testLoadUserByUsername_Success() {
        // Arrange
        when(userService.getUserByUsername(username)).thenReturn(userResponseDTO);

        // Act
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        // Assert
        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertTrue(userDetails.isEnabled());

        // Verify roles
        assertEquals(2, userDetails.getAuthorities().size());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_PARTICIPANT")));
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));

        verify(userService, times(1)).getUserByUsername(username);
    }

    @Test
    public void testLoadUserByUsername_NoRoles() {
        // Arrange
        userResponseDTO.setRoles(null); // Simulate no roles
        when(userService.getUserByUsername(username)).thenReturn(userResponseDTO);

        // Act
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        // Assert
        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertTrue(userDetails.isEnabled());

        // Verify no roles are assigned
        assertEquals(0, userDetails.getAuthorities().size());

        verify(userService, times(1)).getUserByUsername(username);
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        // Arrange
        when(userService.getUserByUsername("unknownuser")).thenReturn(null);

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername("unknownuser"));
        verify(userService, times(1)).getUserByUsername("unknownuser");
    }

    @Test
    public void testLoadUserByUsername_UserNotEnabled() {
        // Arrange
        userResponseDTO.setEnabled(false); // Simulate disabled user
        when(userService.getUserByUsername(username)).thenReturn(userResponseDTO);

        // Act
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        // Assert
        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertFalse(userDetails.isEnabled()); // Verify user is not enabled

        verify(userService, times(1)).getUserByUsername(username);
    }
}