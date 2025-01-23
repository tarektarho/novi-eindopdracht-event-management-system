package nl.novi.event_management_system.models;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class RoleKey implements Serializable {
    private String username;
    private String role;
}

