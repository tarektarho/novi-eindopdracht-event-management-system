package nl.novi.event_management_system.repositories;

import nl.novi.event_management_system.models.Event;
import nl.novi.event_management_system.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
}
