package nl.novi.event_management_system.dtos;

import jakarta.validation.constraints.NotNull;

public class IdInputDTO {
    @NotNull
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
