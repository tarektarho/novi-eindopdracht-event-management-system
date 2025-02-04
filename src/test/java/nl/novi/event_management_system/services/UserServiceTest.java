package nl.novi.event_management_system.services;

import nl.novi.event_management_system.dtos.userDtos.UserCreateDTO;
import nl.novi.event_management_system.dtos.userDtos.UserResponseDTO;
import nl.novi.event_management_system.enums.RoleEnum;
import nl.novi.event_management_system.exceptions.EmailAlreadyExistsException;
import nl.novi.event_management_system.exceptions.RecordNotFoundException;
import nl.novi.event_management_system.exceptions.UsernameNotFoundException;
import nl.novi.event_management_system.mappers.UserMapper;
import nl.novi.event_management_system.models.*;
import nl.novi.event_management_system.repositories.FeedbackRepository;
import nl.novi.event_management_system.repositories.TicketRepository;
import nl.novi.event_management_system.repositories.UserRepository;
import nl.novi.event_management_system.repositories.UserPhotoRepository;
import nl.novi.event_management_system.utils.PasswordGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserPhotoRepository userPhotoRepository;
    @Mock
    private UserPhotoService userPhotoService;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private FeedbackRepository feedbackRepository;

    @InjectMocks
    private UserService userService;


    private List<User> mockUsers;

    private final String username = "testUser";
    private final String email = "tes2t@hotmail.com";
    private final String password = "testPasswordaaaa";

    @BeforeEach
    void setUp() {
        mockUsers = List.of(
                new User(username, email, password),
                new User("testUser2", "test1@hotmail.com", "testPassword2")
        );
    }

    @Test
    void createUserSuccessfullyCreatesNewUser() {
        // Arrange
        UserCreateDTO userCreateDTO = new UserCreateDTO();
        userCreateDTO.setUsername(username);
        userCreateDTO.setEmail(email);
        userCreateDTO.setPassword(password);

        User user = UserMapper.toUserEntity(userCreateDTO);

        try (MockedStatic<PasswordGenerator> passwordGenerator = mockStatic(PasswordGenerator.class)) {
            passwordGenerator.when(() -> PasswordGenerator.generateEncryptedPassword(anyString())).thenReturn(password);

            when(userRepository.save(any(User.class))).thenReturn(user);

            // Act
            UserResponseDTO userResponseDTO = userService.createUser(userCreateDTO);

            // Assert
            assertNotNull(userResponseDTO);
            assertEquals(username, userResponseDTO.getUsername());
            assertEquals(email, userResponseDTO.getEmail());
            assertEquals(user.getRoles(), userResponseDTO.getRoles());
        }

    }

    @Test
    void createUserThrowsExceptionWhenEmailAlreadyExists() {
        // Arrange
        UserCreateDTO userCreateDTO = new UserCreateDTO();
        userCreateDTO.setUsername(username);
        userCreateDTO.setEmail(email);
        userCreateDTO.setPassword(password);

        when(userRepository.existsByEmail(email)).thenReturn(true);

        // Act & Assert
        assertThrows(EmailAlreadyExistsException.class, () -> userService.createUser(userCreateDTO));
    }

    @Test
    void getAllUsersReturnsAllUsers() {
        // Arrange
        when(userRepository.findAll()).thenReturn(mockUsers);

        // Act
        List<UserResponseDTO> users = userService.getAllUsers();

        // Assert
        assertEquals(2, users.size());
    }

    @Test
    void getUserByUsernameReturnsUser() {
        // Arrange
        User user = new User(username, email, password);
        user.setRoles(Set.of(new Role("admin", RoleEnum.getRoleName(RoleEnum.ADMIN))));

        when(userRepository.findByUsername(username)).thenReturn(java.util.Optional.of(user));

        // Act
        UserResponseDTO userResponseDTO = userService.getUserByUsername(username);

        // Assert
        assertNotNull(userResponseDTO);
        assertEquals(username, userResponseDTO.getUsername());
        assertEquals(email, userResponseDTO.getEmail());
        assertEquals(user.getRoles(), userResponseDTO.getRoles());
    }

    @Test
    void getUserByUsernameThrowsExceptionWhenUserNotFound() {
        // Arrange
        when(userRepository.findByUsername(username)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> userService.getUserByUsername(username));
    }

    @Test
    void updateUserSuccessfullyUpdatesUser() {

        // Arrange
        UserCreateDTO newUser = new UserCreateDTO();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(password);

        User user = new User(username, email, password);

        when(userRepository.existsById(username)).thenReturn(true);
        when(userRepository.findById(username)).thenReturn(java.util.Optional.of(user));

        // Act
        userService.updateUser(username, newUser);

        // Assert
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUserThrowsExceptionWhenUserNotFound() {
        // Arrange
        when(userRepository.existsById(username)).thenReturn(false);

        // Act & Assert
        assertThrows(RecordNotFoundException.class, () -> userService.updateUser(username, new UserCreateDTO()));
    }

    @Test
    void deleteUserSuccessfullyDeletesUser() {
        // Arrange
        when(userRepository.existsByUsername(username)).thenReturn(true);

        // Act
        userService.deleteUser(username);

        // Assert
        verify(userRepository).deleteByUsername(username);
    }

    @Test
    void deleteUserThrowsExceptionWhenUserNotFound() {
        // Arrange
        when(userRepository.existsByUsername(username)).thenReturn(false);

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> userService.deleteUser(username));
    }

    @Test
    void getRolesReturnsRoles() {
        // Arrange
        User user = new User(username, email, password);
        user.setRoles(Set.of(new Role("admin", RoleEnum.getRoleName(RoleEnum.ADMIN))));

        when(userRepository.existsById(username)).thenReturn(true);
        when(userRepository.findById(username)).thenReturn(java.util.Optional.of(user));

        // Act
        Set<Role> roles = userService.getRoles(username);

        // Assert
        assertNotNull(roles);
        assertEquals(user.getRoles(), roles);
    }

    @Test
    void getRolesThrowsExceptionWhenUserNotFound() {
        // Arrange
        when(userRepository.existsById(username)).thenReturn(false);

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> userService.getRoles(username));
    }

    @Test
    void addRoleSuccessfullyAddsRole() {
        // Arrange
        User user = new User(username, email, password);

        when(userRepository.existsById(username)).thenReturn(true);
        when(userRepository.findById(username)).thenReturn(java.util.Optional.of(user));

        // Act
        userService.addRole(username, "admin");

        // Assert
        verify(userRepository).save(any(User.class));
    }

    @Test
    void addRoleThrowsExceptionWhenUserNotFound() {
        // Arrange
        when(userRepository.existsById(username)).thenReturn(false);

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> userService.addRole(username, "admin"));
    }

    @Test
    void removeRoleSuccessfullyRemovesRole() {
        // Arrange
        User user = new User(username, email, password);
        user.addRole(new Role(username, "admin"));

        when(userRepository.existsById(username)).thenReturn(true);
        when(userRepository.findById(username)).thenReturn(java.util.Optional.of(user));

        // Act
        userService.removeRole(username, "admin");

        // Assert
        verify(userRepository).save(any(User.class));
    }

    @Test
    void removeRoleThrowsExceptionWhenUserNotFound() {
        // Arrange
        when(userRepository.existsById(username)).thenReturn(false);

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> userService.removeRole(username, "admin"));
    }

    @Test
    void assignPhotoToUserSuccessfullyAssignsPhotoToUser() {
        // Arrange
        String fileName = "test.jpg";
        User user = new User(username, email, password);

        UserPhoto userPhoto = new UserPhoto(fileName);

        when(userPhotoRepository.findByFileName(fileName)).thenReturn(Optional.of(userPhoto));
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        userService.assignPhotoToUser(fileName, username);

        // Assert
        verify(userRepository).save(any(User.class));
    }

    @Test
    void assignPhotoToUserThrowsExceptionWhenUserOrPhotoNotFound() {
        // Arrange
        String fileName = "test.jpg";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(userPhotoRepository.findByFileName(fileName)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RecordNotFoundException.class, () -> userService.assignPhotoToUser(fileName, username));
    }

    @Test
    void getUserPhotoShouldReturnUserPhoto() {
        // Arrange
        String username = "testUser";
        UserPhoto userPhoto = new UserPhoto();
        userPhoto.setFileName("testPhoto.jpg");

        User user = new User();
        user.setUsername(username);
        user.setUserPhoto(userPhoto);

        Resource mockResource = mock(Resource.class);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(userPhotoService.downLoadFile("testPhoto.jpg")).thenReturn(mockResource);

        // Act
        Resource result = userService.getUserPhoto(username);

        // Assert
        assertNotNull(result);
        assertEquals(mockResource, result);
        verify(userRepository).findByUsername(username);
        verify(userPhotoService).downLoadFile("testPhoto.jpg");
    }

    @Test
    void getUserPhotoShouldThrowExceptionWhenUserNotFound() {
        // Arrange
        String username = "testUser";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RecordNotFoundException.class, () -> userService.getUserPhoto(username));
    }

    @Test
    void getUserPhotoShouldThrowExceptionWhenUserHasNoPhoto() {
        // Arrange
        String username = "testUser";
        User user = new User();
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(RecordNotFoundException.class, () -> userService.getUserPhoto(username));
    }


    @Test
    void assignTicketToUser() {
        // Arrange
        User user = new User(username, email, password);
        user.setUsername(username);
        user.setRoles(Set.of(new Role("admin", RoleEnum.getRoleName(RoleEnum.ADMIN))));
        when(userRepository.save(any(User.class))).thenReturn(user);

        Ticket ticket = new Ticket();
        UUID id = UUID.randomUUID();
        ticket.setId(id);


        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(ticketRepository.findById(any())).thenReturn(Optional.of(ticket));


        // Act
        UserResponseDTO userResponseDTO = userService.assignTicketToUser(username, id);

        // Assert
        assertNotNull(userResponseDTO);
        assertEquals(username, userResponseDTO.getUsername());
        assertEquals(email, userResponseDTO.getEmail());
        assertEquals(user.getRoles(), userResponseDTO.getRoles());
    }

    @Test
    void assignTicketToUserThrowsExceptionWhenUserNotFound() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RecordNotFoundException.class, () -> userService.assignTicketToUser(username, id));
    }

    @Test
    void assignFeedbackToUser() {
        // Arrange
        User user = new User(username, email, password);
        user.setUsername(username);
        user.setRoles(Set.of(new Role("admin", RoleEnum.getRoleName(RoleEnum.ADMIN))));

        when(userRepository.save(any(User.class))).thenReturn(user);

        Feedback feedback = new Feedback();
        UUID id = UUID.randomUUID();
        feedback.setId(id);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(feedbackRepository.findById(any())).thenReturn(Optional.of(feedback));

        // Act
        UserResponseDTO userResponseDTO = userService.assignFeedbackToUser(username, id);

        // Assert
        assertNotNull(userResponseDTO);
        assertEquals(username, userResponseDTO.getUsername());
        assertEquals(email, userResponseDTO.getEmail());
        assertEquals(user.getRoles(), userResponseDTO.getRoles());
    }

    @Test
    void assignFeedbackToUserThrowsExceptionWhenUserNotFound() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> userService.assignFeedbackToUser(username, id));
    }

}