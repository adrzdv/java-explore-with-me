package ru.practicum.enw.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.enw.model.entity.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepo extends JpaRepository<Event, Long> {

    @Query("SELECT e FROM Event AS e WHERE e.initiator.id = :idUser " +
            "ORDER BY e.id LIMIT :size OFFSET :from")
    List<Event> getEventsWithParams(long idUser, int from, int size);

    Event findEventByIdAndInitiatorId(long eventId, long initiatorId);

    @Query("SELECT e FROM Event AS e WHERE " +
            "(:users IS NULL OR e.initiator IN (SELECT u FROM User AS u WHERE u.id IN :users)) " +
            "AND (:states IS NULL OR e.state IN :states) " +
            "AND (:categories IS NULL OR e.category IN (SELECT c FROM Category AS c WHERE c.id IN :categories)) " +
            "AND e.eventDate BETWEEN :rangeStart AND :rangeEnd " +
            "ORDER BY e.views DESC LIMIT :size OFFSET :from")
    List<Event> findAllEventsForAdminHavingParams(List<Long> users, List<String> states, List<Long> categories,
                                                  LocalDateTime rangeStart, LocalDateTime rangeEnd, int from,
                                                  int size);

    @Query("SELECT e FROM Event AS e WHERE" +
            "(:text IS NULL OR e.annotation ILIKE %:text% AND e.description ILIKE %:text%) " +
            "AND (:categories IS NULL OR e.category IN (SELECT c FROM Category AS c WHERE c.id IN :categories)) " +
            "AND (:paid IS NULL OR e.paid = :paid) " +
            "AND (:isAvailable = false OR e.participantLimit > e.confirmedRequests) " +
            "AND e.state = 'PUBLISHED' " +
            "AND e.eventDate BETWEEN :rangeStart AND :rangeEnd " +
            "ORDER BY " +
            "CASE WHEN :sort = 'EVENT_DATE' THEN e.eventDate END ASC, " +
            "CASE WHEN :sort = 'VIEWS' THEN e.views END DESC, " +
            "CASE WHEN :sort IS NULL THEN e.id END ASC " +
            "LIMIT :size OFFSET :from")
    List<Event> getAllEventsByFiltersForPublic(String text, List<Integer> categories, Boolean paid, Boolean isAvailable,
                                               String sort, LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                               int from, int size);

    @Query("SELECT e FROM Event AS e WHERE" +
            "(:text IS NULL OR (e.annotation ILIKE %:text% AND e.description ILIKE %:text%)) " +
            "AND (:categories IS NULL OR e.category IN (SELECT c FROM Category AS c WHERE c.id IN :categories)) " +
            "AND (:paid IS NULL OR e.paid = :paid) " +
            "AND (:isAvailable = false OR e.participantLimit > e.confirmedRequests) " +
            "AND e.state = 'PUBLISHED' " +
            "AND e.eventDate > :now " +
            "ORDER BY " +
            "CASE WHEN :sort = 'EVENT_DATE' THEN e.eventDate END ASC, " +
            "CASE WHEN :sort = 'VIEWS' THEN e.views END DESC, " +
            "CASE WHEN :sort IS NULL THEN e.id END ASC " +
            "LIMIT :size OFFSET :from")
    List<Event> getAllEventsFromCurrentDate(String text, List<Integer> categories, Boolean paid, Boolean isAvailable,
                                            String sort, LocalDateTime now, int from, int size);

    List<Event> findByIdIn(List<Long> ids);

    List<Event> findByCategoryId(long id);


}
