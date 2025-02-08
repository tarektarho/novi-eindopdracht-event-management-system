package nl.novi.event_management_system.models;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class RoleKey implements Serializable {
    private String username;
    private String role;

    public RoleKey() {
    }

    public RoleKey(String username, String role) {
        this.username = username;
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleKey roleKey = (RoleKey) o;
        return Objects.equals(username, roleKey.username) && Objects.equals(role, roleKey.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, role);
    }
}
