package nl.novi.event_management_system.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import nl.novi.event_management_system.dtos.authentication.AuthenticationRequest;
import nl.novi.event_management_system.dtos.authentication.AuthenticationResponse;
import nl.novi.event_management_system.services.CustomUserDetailsService;
import nl.novi.event_management_system.utils.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * Controller for handling authentication-related endpoints.
 */
@Tag(name = "Authentication API", description = "Authentication related endpoints")
@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtl;

    /**
     * Constructs an AuthenticationController with the specified dependencies.
     *
     * @param authenticationManager the authentication manager
     * @param userDetailsService the custom user details service
     * @param jwtUtl the JWT utility
     */
    public AuthenticationController(AuthenticationManager authenticationManager, CustomUserDetailsService userDetailsService, JwtUtil jwtUtl) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtl = jwtUtl;
    }

    /**
     * Endpoint to check if the user is authenticated.
     *
     * @param authentication the authentication object
     * @param principal the principal object
     * @return a ResponseEntity containing the principal
     */
    @GetMapping(value = "/authenticated")
    public ResponseEntity<Object> authenticated(Authentication authentication, Principal principal) {
        return ResponseEntity.ok().body(principal);
    }

    /**
     * Endpoint to authenticate a user and generate a JWT token.
     *
     * @param authenticationRequest the authentication request containing username and password
     * @return a ResponseEntity containing the authentication response with the JWT token
     * @throws Exception if authentication fails
     */
    @PostMapping(value = "/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

        String username = authenticationRequest.getUsername();
        String password = authenticationRequest.getPassword();

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
        } catch (BadCredentialsException ex) {
            throw new Exception("Incorrect username or password", ex);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        final String jwt = jwtUtl.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}