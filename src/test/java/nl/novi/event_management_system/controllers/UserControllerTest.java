package nl.novi.event_management_system.controllers;

import nl.novi.event_management_system.dtos.RoleCreateDTO;
import nl.novi.event_management_system.dtos.userDtos.UserCreateDTO;
import nl.novi.event_management_system.dtos.userDtos.UserResponseDTO;
import nl.novi.event_management_system.mappers.UserMapper;
import nl.novi.event_management_system.models.Role;
import nl.novi.event_management_system.models.User;
import nl.novi.event_management_system.models.UserPhoto;
import nl.novi.event_management_system.repositories.UserPhotoRepository;
import nl.novi.event_management_system.repositories.UserRepository;
import nl.novi.event_management_system.services.UserPhotoService;
import nl.novi.event_management_system.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserPhotoService userPhotoService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserPhotoRepository userPhotoRepository;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser_Success() {
        // Arrange
        UserCreateDTO userCreateDTO = new UserCreateDTO();
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setUsername("testUser");

        when(userService.createUser(any(UserCreateDTO.class))).thenReturn(userResponseDTO);

        // Mock HTTP request context
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);

        // Act
        ResponseEntity<?> response = userController.createUser(userCreateDTO, result);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(userResponseDTO, response.getBody());
        assertNotNull(response.getHeaders().getLocation());
    }

    @Test
    void testCreateUser_Failure() {
        // Arrange
        UserCreateDTO userCreateDTO = new UserCreateDTO();
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);

        // Act
        ResponseEntity<?> response = userController.createUser(userCreateDTO, result);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetAllUsers_Success() {
        // Arrange
        List<UserResponseDTO> userList = Collections.singletonList(new UserResponseDTO());
        when(userService.getAllUsers()).thenReturn(userList);

        // Act
        ResponseEntity<List<UserResponseDTO>> response = userController.getAllUsers();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userList, response.getBody());
    }

    @Test
    void testGetUser_ByUsername_Success() {
        // Arrange
        String username = "testUser";
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setUsername(username);

        when(userService.getUserByUsername(username)).thenReturn(userResponseDTO);

        // Act
        ResponseEntity<UserResponseDTO> response = userController.getUserByUsername(username);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userResponseDTO, response.getBody());
    }

    @Test
    void testUpdateUser_Success() {
        // Arrange
        String username = "testUser";
        UserCreateDTO userCreateDTO = new UserCreateDTO();

        // Act
        ResponseEntity<UserResponseDTO> response = userController.updateUser(username, userCreateDTO);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService, times(1)).updateUser(username, userCreateDTO);
    }

    @Test
    void testGetUserByUsernameRoles_Success() {
        // Arrange
        String username = "testUser";
        Set<Role> roles = Collections.singleton(new Role("admin", "ROLE_ADMIN"));

        when(userService.getUserRoles(username)).thenReturn(roles);

        // Act
        ResponseEntity<Object> response = userController.getUserRoles(username);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(roles, response.getBody());
    }

    @Test
    void testAddRole_ToUser_Success() {
        // Arrange
        String username = "testUser";
        RoleCreateDTO roleCreateDTO = new RoleCreateDTO();
        roleCreateDTO.setRole("ROLE_ADMIN");

        // Act
        ResponseEntity<Void> response = userController.addRoleToUser(username, roleCreateDTO);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService, times(1)).addRoleToUser(username, roleCreateDTO.getRole());
    }

    @Test
    void testDeleteUserRole_Success() {
        // Arrange
        String username = "testUser";
        String role = "ROLE_ADMIN";

        // Act
        ResponseEntity<Void> response = userController.deleteUserRole(username, role);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService, times(1)).deleteUserRole(username, role);
    }

    @Test
    void testDeleteUser_Success() {
        // Arrange
        String username = "testUser";

        // Act
        ResponseEntity<Void> response = userController.deleteUser(username);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService, times(1)).deleteUser(username);
    }


    @Test
    void testAddPhotoToUser_Success() throws IOException {
        // Arrange
        String username = "testUser";
        String fileName = "test.jpg";

        MultipartFile file = new MockMultipartFile("file", fileName, "image/jpeg", "test image content".getBytes());

        // Mock User entity
        User user = new User();
        user.setUsername(username);

        // Mock UserPhoto entity
        UserPhoto userPhoto = new UserPhoto();
        userPhoto.setFileName(fileName);

        // Mock repository responses
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(userPhotoRepository.findByFileName(anyString())).thenReturn(Optional.of(userPhoto));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userPhotoService.storeFile(file)).thenReturn(fileName);

        // Mock `userService.assignPhotoToUser()`
        when(userService.assignPhotoToUser(anyString(), eq(username))).thenReturn(user);

        // Mock HTTP request context
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        // Act
        ResponseEntity<UserResponseDTO> response = userController.addPhotoToUser(username, file);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getHeaders().getLocation());
        assertEquals(UserMapper.toUserResponseDTO(user), response.getBody());
    }


    @Test
    void testGetUser_Photo_Success() {
        // Arrange
        String username = "testUser";
        Resource resource = mock(Resource.class);
        when(resource.getFilename()).thenReturn("test.jpg");

        when(userPhotoService.downLoadFile(anyString())).thenReturn(resource);
        when(userService.getUserPhoto(username)).thenReturn(resource);
        // Mock HTTP request context
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        // Act
        ResponseEntity<Resource> response = userController.getUserPhoto(username, request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(resource, response.getBody());
        assertNotNull(response.getHeaders().get(HttpHeaders.CONTENT_DISPOSITION));
        assertNotNull(response.getHeaders().get(HttpHeaders.CONTENT_TYPE));
    }

    @Test
    void testAssignTicketToUser_Success() {
        // Arrange
        String username = "testUser";
        UUID ticketId = UUID.randomUUID();
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setUsername(username);

        when(userService.assignTicketToUser(username, ticketId)).thenReturn(userResponseDTO);

        // Act
        ResponseEntity<UserResponseDTO> response = userController.assignTicketToUser(username, ticketId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userResponseDTO, response.getBody());
    }

}