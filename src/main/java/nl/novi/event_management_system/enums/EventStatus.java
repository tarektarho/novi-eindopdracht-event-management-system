package nl.novi.event_management_system.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum EventStatus {
    POSTPONED,
    ACTIVE,
    CANCELLED,
    DELETED,
    FINISHED,
    UPCOMING;
}

