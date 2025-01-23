package nl.novi.event_management_system.repositories;

import nl.novi.event_management_system.models.Reservation;
import nl.novi.event_management_system.models.User;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    //Optional<Reservation> findByIdAndParticipants(long id, List<User> participants);
    Optional<Reservation> findByIdAndEventId(Long Id, Long eventId);
}
