package ru.practicum.requests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.requests.model.ParticipationRequest;
import ru.practicum.requests.model.RequestStatus;

import java.util.List;
import java.util.Map;

@Repository
public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {

    boolean existsByEventIdAndRequesterId(Long eventId, Long requesterId);

    List<ParticipationRequest> findByRequesterId(Long requesterId);

    List<ParticipationRequest> findAllByEventId(Long eventId);

    @Query("SELECT r.event.id, COUNT(r) FROM ParticipationRequest r " +
            "WHERE r.status = :status AND r.event.id IN :eventIds " +
            "GROUP BY r.event.id")
    Map<Long, Long> countConfirmedRequestsByEventIds(@Param("eventIds") List<Long> eventIds,
                                                     @Param("status") RequestStatus status);

    @Query("SELECT COUNT(r) FROM ParticipationRequest r WHERE r.event.id = :eventId AND r.status = :status")
    Long requestsCountByEventAndStatusId(@Param("eventId") Long eventId,
                                       @Param("status") RequestStatus status);
}
