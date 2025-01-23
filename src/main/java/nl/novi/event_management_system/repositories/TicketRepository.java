package nl.novi.event_management_system.repositories;

import nl.novi.event_management_system.models.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findTicketByEventId(Long eventId);
    List<Ticket> findTicketByUserUsername(String username);

}
