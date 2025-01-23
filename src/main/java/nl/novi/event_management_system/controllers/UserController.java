package nl.novi.event_management_system.controllers;

import jakarta.validation.Valid;
import nl.novi.event_management_system.dtos.UserDTO;
import nl.novi.event_management_system.exceptions.BadRequestException;
import nl.novi.event_management_system.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getUsers() {
        List<UserDTO> users;

        try {
            users = userService.getUsers();
        } catch (Exception e) {
            // Log error
            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(users);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserDTO> getUser(@PathVariable("username") String username) {
        UserDTO user;

        try {
            user = userService.getUser(username);
        } catch (Exception e) {
            // Log error
            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserDTO userDTO, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        String newUsername = userService.createUser(userDTO);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{username}")
                .buildAndExpand(newUsername).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{username}")
    public ResponseEntity<Object> updateUser(@PathVariable String username, @Valid @RequestBody UserDTO userDTO) {

        try {
            userService.updateUser(username, userDTO);
        } catch (Exception e) {
            // Log error
            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();

    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping( "/{username}/roles")
    public ResponseEntity<Object> getUserRoles(@PathVariable("username") String username) {
        return ResponseEntity.ok().body(userService.getRoles(username));
    }


    @PostMapping(value = "/{username}/roles")
    public ResponseEntity<Object> addUserRole(@PathVariable("username") String username, @RequestBody Map<String, Object> fields) {
        // Role has to be prefixed with "ROLE_" to be compliant with Spring Security
        try {
            String roleName = (String) fields.get("role");
            userService.addRole(username, roleName);
            return ResponseEntity.noContent().build();
        }
        catch (BadRequestException ex) {
            throw new BadRequestException();
        }
    }

    @DeleteMapping(value = "/{username}/roles/{role}")
    public ResponseEntity<Object> deleteUserRole(@PathVariable("username") String username, @PathVariable("role") String role) {
        userService.removeRole(username, role);
        return ResponseEntity.noContent().build();
    }

}
