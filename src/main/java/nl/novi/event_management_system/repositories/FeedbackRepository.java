package nl.novi.event_management_system.repositories;

import nl.novi.event_management_system.models.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, UUID> {
    List<Feedback> findByEventId(UUID eventId);
    List<Feedback> findByUserUsername(String username);
}
