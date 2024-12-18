package ru.practicum.enw.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.enw.model.entity.ParticipationRequest;

@Repository
public interface RequestRepo extends JpaRepository<ParticipationRequest, Long> {

    List<ParticipationRequest> findAllByRequester(long idUser);

    List<ParticipationRequest> findAllByEvent(long idEvent);

    @Query("SELECT p FROM ParticipationRequest p WHERE p.id IN :ids ORDER BY p.created ASC")
    List<ParticipationRequest> findCurrentRequests(List<Long> ids);

    ParticipationRequest findByEventAndRequester(long idEvent, long idRequester);
}
