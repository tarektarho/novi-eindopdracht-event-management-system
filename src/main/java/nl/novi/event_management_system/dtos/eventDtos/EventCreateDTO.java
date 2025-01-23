package nl.novi.event_management_system.dtos.eventDtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import nl.novi.event_management_system.enums.EventStatus;
import nl.novi.event_management_system.validators.eventStatus.ValidEventStatus;

import java.time.LocalDate;

public class EventCreateDTO {

    @NotBlank(message = "Name is mandatory")
    private String name;
    @NotBlank(message = "Location is mandatory")
    private String location;

    private String description;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate startDate;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate endDate;

    private Integer maxParticipants;

    @ValidEventStatus(message = "Invalid status. Please provide one of the following: ACTIVE, CANCELLED, FINISHED, DELETED, UPCOMING, POSTPONED.")
    private EventStatus status;


    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Integer getMaxParticipants() {
        return maxParticipants;
    }

    public EventStatus getStatus() {
        return status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setMaxParticipants(Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }

}
