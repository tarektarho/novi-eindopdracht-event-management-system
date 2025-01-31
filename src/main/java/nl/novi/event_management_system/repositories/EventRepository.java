package nl.novi.event_management_system.repositories;

import nl.novi.event_management_system.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {
    Optional<Event> findEventById(UUID id);
    List<Event> findByOrganizerUsername(String username);

}
