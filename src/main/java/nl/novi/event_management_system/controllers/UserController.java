package nl.novi.event_management_system.controllers;

import io.swagger.v3.oas.annotations.tags.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import nl.novi.event_management_system.dtos.RoleCreateDTO;
import nl.novi.event_management_system.dtos.userDtos.UserCreateDTO;
import nl.novi.event_management_system.dtos.userDtos.UserResponseDTO;
import nl.novi.event_management_system.mappers.UserMapper;
import nl.novi.event_management_system.models.User;
import nl.novi.event_management_system.services.UserPhotoService;
import nl.novi.event_management_system.services.UserService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.*;

@Tag(name = "User API", description = "User related endpoints")
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final UserPhotoService userPhotoService;

    /**
     * Constructor
     *
     * @param userService      UserService
     * @param userPhotoService UserPhotoService
     */
    public UserController(UserService userService, UserPhotoService userPhotoService) {
        this.userService = userService;
        this.userPhotoService = userPhotoService;
    }

    /**
     * Create a new user
     *
     * @param userCreateDTO UserCreateDTO
     * @return ResponseEntity<UserResponseDTO>
     */
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserCreateDTO userCreateDTO) {
        UserResponseDTO newUser = userService.createUser(userCreateDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{username}")
                .buildAndExpand(newUser.getUsername()).toUri();
        return ResponseEntity.created(location).body(newUser);
    }

    /**
     * Get all users
     *
     * @return ResponseEntity<List < UserResponseDTO>>
     */
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok().body(users);
    }

    /**
     * Get a user by username
     *
     * @param username String
     * @return ResponseEntity<UserResponseDTO>
     */
    @GetMapping("/{username}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable String username) {
        return ResponseEntity.ok().body(userService.getUserByUsername(username));
    }

    /**
     * Update a user
     *
     * @param username      String
     * @param userCreateDTO UserCreateDTO
     * @return ResponseEntity<UserResponseDTO>
     */
    @PutMapping(value = "/{username}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable("username") String username, @RequestBody UserCreateDTO userCreateDTO) {
        userService.updateUser(username, userCreateDTO);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get all roles of a user
     *
     * @param username String
     * @return ResponseEntity<Object>
     */
    @GetMapping(value = "/{username}/roles")
    public ResponseEntity<Object> getUserRoles(@PathVariable("username") String username) {
        return ResponseEntity.ok().body(userService.getRoles(username));
    }

    /**
     * Add a role to a user
     *
     * @param username      String
     * @param roleCreateDTO RoleCreateDTO
     * @return ResponseEntity<Void>
     */
    @PostMapping("/{username}/roles")
    public ResponseEntity<Void> addUserRole(
            @PathVariable("username") String username,
            @Valid @RequestBody RoleCreateDTO roleCreateDTO) {

        userService.addRole(username, roleCreateDTO.getRole());
        return ResponseEntity.noContent().build();
    }

    /**
     *  Delete a role from a user
     *
     * @param username String
     * @param role     String
     * @return ResponseEntity<Object>
     */
    @DeleteMapping(value = "/{username}/roles/{role}")
    public ResponseEntity<Void> deleteUserRole(@PathVariable("username") String username, @PathVariable("role") String role) {
        userService.removeRole(username, role);
        return ResponseEntity.noContent().build();
    }

    /**
     * Delete a user
     *
     * @param username String
     * @return String
     */
    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }

    /**
     * Add a photo to a user
     *
     * @param username String
     * @param file     MultipartFile
     * @return ResponseEntity<UserResponseDTO>
     * @throws IOException IOException
     */
    @PostMapping("/{username}/photo")
    public ResponseEntity<UserResponseDTO> addPhotoToUser(
            @PathVariable String username,
            @RequestParam("file") MultipartFile file) throws IOException {

        String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/users/")
                .path(username)
                .path("/photo")
                .toUriString();

        String fileName = userPhotoService.storeFile(file);
        User user = userService.assignPhotoToUser(fileName, username);

        return ResponseEntity.created(URI.create(url))
                .body(UserMapper.toUserResponseDTO(user));
    }

    /**
     * Get the photo of a user
     *
     * @param username String
     * @param request  HttpServletRequest
     * @return ResponseEntity<Resource>
     */
    @GetMapping("/{username}/photo")
    public ResponseEntity<Resource> getPhotoOfUser(@PathVariable String username, HttpServletRequest request) {
        Resource resource = userService.getUserPhoto(username);

        // Determine file MIME type
        String mimeType = Optional.ofNullable(request.getServletContext().getMimeType(resource.getFilename()))
                .orElse("application/octet-stream");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, mimeType)
                .body(resource);
    }

    /**
     * Delete the photo of a user
     *
     * @param username String
     * @return ResponseEntity<Object>
     */
    @PostMapping("/{username}/ticket/{ticketId}")
    public ResponseEntity<UserResponseDTO> assignTicketToUser(@PathVariable String username, @PathVariable UUID ticketId) {
        return ResponseEntity.ok().body(userService.assignTicketToUser(username, ticketId));
    }

    /**
     * Assign a feedback to a user
     *
     * @param username   String
     * @param feedbackId UUID
     * @return ResponseEntity<UserResponseDTO>
     */
    @PostMapping("/{username}/feedback/{feedbackId}")
    public ResponseEntity<UserResponseDTO> assignFeedbackToUser(@PathVariable String username, @PathVariable UUID feedbackId) {
        return ResponseEntity.ok().body(userService.assignFeedbackToUser(username, feedbackId));
    }

}
