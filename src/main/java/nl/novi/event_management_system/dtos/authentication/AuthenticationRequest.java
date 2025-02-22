package nl.novi.event_management_system.dtos.authentication;


import jakarta.validation.constraints.NotNull;

public class AuthenticationRequest {
    @NotNull(message = "Username is mandatory")
    private String username;
    @NotNull(message = "Password is mandatory")
    private String password;

    public AuthenticationRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}