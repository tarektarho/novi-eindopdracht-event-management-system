package nl.novi.event_management_system.controllers;

import io.swagger.v3.oas.annotations.tags.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import nl.novi.event_management_system.dtos.RoleCreateDTO;
import nl.novi.event_management_system.dtos.userDtos.UserCreateDTO;
import nl.novi.event_management_system.dtos.userDtos.UserResponseDTO;
import nl.novi.event_management_system.exceptions.BadRequestException;
import nl.novi.event_management_system.mappers.UserMapper;
import nl.novi.event_management_system.models.Role;
import nl.novi.event_management_system.models.User;
import nl.novi.event_management_system.services.UserPhotoService;
import nl.novi.event_management_system.services.UserService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.*;

@Slf4j
@Tag(name = "User API", description = "User related endpoints")
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final UserPhotoService userPhotoService;

    public UserController(UserService userService, UserPhotoService userPhotoService) {
        this.userService = userService;
        this.userPhotoService = userPhotoService;
    }

    @PostMapping("/create")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserCreateDTO userCreateDTO) {
        UserResponseDTO newUser = userService.createUser(userCreateDTO);
        Set<Role> roleList = userCreateDTO.getRoles();
        for (Role role : roleList) {
            if (role != null) {
                userService.addRole(newUser.getUsername(), role.getRole());
            }
        }
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{username}")
                .buildAndExpand(newUser.getUsername()).toUri();
        return ResponseEntity.created(location).body(newUser);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok().body(users);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable String username) {
        return ResponseEntity.ok().body(userService.getUserByUsername(username));
    }

    @PutMapping(value = "/{username}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable("username") String username, @RequestBody UserCreateDTO userCreateDTO) {

        userService.updateUser(username, userCreateDTO);

        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{username}/roles")
    public ResponseEntity<Object> getUserRoles(@PathVariable("username") String username) {
        return ResponseEntity.ok().body(userService.getRoles(username));
    }


    @PostMapping("/{username}/roles")
    public ResponseEntity<Object> addUserRole(
            @PathVariable("username") String username,
            @Valid @RequestBody RoleCreateDTO roleCreateDTO) {

        try {
            userService.addRole(username, roleCreateDTO.getRole());
            return ResponseEntity.noContent().build();
        } catch (BadRequestException ex) {
            log.error("Failed to add role '{}' to user '{}': {}", roleCreateDTO.getRole(), username, ex.getMessage());
            return ResponseEntity.badRequest().body("Error: " + ex.getMessage());
        } catch (Exception ex) {
            log.error("Unexpected error while assigning role to user '{}': {}", username, ex.getMessage());
            return ResponseEntity.internalServerError().body("An unexpected error occurred.");
        }
    }

    @DeleteMapping(value = "/{username}/roles/{role}")
    public ResponseEntity<Object> deleteUserRole(@PathVariable("username") String username, @PathVariable("role") String role) {
        userService.removeRole(username, role);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{username}")
    public String deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
        return "User deleted successfully!";
    }

    @PostMapping("/{username}/photo")
    public ResponseEntity<UserResponseDTO> addPhotoToUser(@PathVariable String username, @RequestParam("file") MultipartFile file) throws IOException {
        String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/users/")
                .path(Objects.requireNonNull(username))
                .path("/photo")
                .toUriString();
        String fileName;
        User user;

        try {
            fileName = userPhotoService.storeFile(file);
        } catch (BadRequestException ex) {
            log.error("Error: " + ex.getMessage());
            return  ResponseEntity.badRequest().build();
        }

        try {
            user = userService.assignPhotoToUser(fileName, username);
        } catch (BadRequestException ex) {
            log.error("Error: " + ex.getMessage());
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.created(URI.create(url)).body(UserMapper.toUserResponseDTO(user));
    }

    @GetMapping("/{username}/photo")
    public ResponseEntity<Resource> getPhotoOfUser(@PathVariable String username, HttpServletRequest request) {
        Resource resource = userService.getUserPhoto(username);

        String mineType;

        try {
            mineType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            mineType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, mineType)
                .body(resource);
    }

    @PatchMapping("/{username}/ticket/{ticketId}")
    public ResponseEntity<UserResponseDTO> assignTicketToUser(@PathVariable String username, @PathVariable UUID ticketId) {
        return ResponseEntity.ok().body(userService.assignTicketToUser(username, ticketId));
    }

    @PatchMapping("/{username}/feedback/{feedbackId}")
    public ResponseEntity<UserResponseDTO> assignFeedbackToUser(@PathVariable String username, @PathVariable UUID feedbackId) {
        return ResponseEntity.ok().body(userService.assignFeedbackToUser(username, feedbackId));
    }

}
