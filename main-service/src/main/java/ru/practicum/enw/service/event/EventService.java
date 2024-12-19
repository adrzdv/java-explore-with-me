package ru.practicum.enw.service.event;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.enw.exceptions.NotFoundCustomException;
import ru.practicum.enw.model.enums.SortType;
import ru.practicum.enw.model.event.*;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    /**
     * Add new event
     *
     * @param event NewEventDro object for adding
     * @param id    identification number of initiator
     * @return EventFullDto object
     * @throws NotFoundCustomException
     */
    EventFullDto add(NewEventDto event, long id) throws NotFoundCustomException;

    /**
     * Get initiators events
     *
     * @param idUser initiator's identification number
     * @param from   the number of skipped records
     * @param size   the number of list size
     * @return List of EventShortDto object
     */
    List<EventShortDto> getUsersEvents(long idUser, int from, int size);

    /**
     * Get initiator event by event's id
     *
     * @param idUser  initiators' identification number
     * @param idEvent identification number of event
     * @return EventFullDto object
     * @throws NotFoundCustomException
     */
    EventFullDto getUsersEvent(long idUser, long idEvent) throws NotFoundCustomException;

    /**
     * Update an existing user's event
     *
     * @param userId  initiators id
     * @param eventId event's identification number
     * @param event   UpdateEventUserRequest object
     * @return EventFullDto object
     * @throws NotFoundCustomException
     */
    EventFullDto updateUsersEvent(long userId, long eventId, UpdateEventUserRequest event)
            throws NotFoundCustomException;

    /**
     * Update an existing event by admin
     *
     * @param id    identification number of event
     * @param event UpdateEventAdminRequest object
     * @return EventFullDto object
     * @throws NotFoundCustomException
     */
    EventFullDto updateEventByAdmin(long id, UpdateEventAdminRequest event) throws NotFoundCustomException;

    /**
     * Get all events having parameters and filters for admin controller
     *
     * @param users      List of initiator's ids
     * @param states     List of states
     * @param categories List of event's categories ids
     * @param rangeStart Start filtering date
     * @param rangeEnd   End filtering date
     * @param from       the number of skipped records
     * @param size       the number of result list size
     * @return List of EventFullDto objects
     */
    List<EventFullDto> getAdminAllEvents(List<Long> users, List<String> states, List<Long> categories,
                                         LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);

    /**
     * Get list of events having parameters and filters for public controller
     *
     * @param text          searching text in annotation or description
     * @param categories    list of categories ids
     * @param paid          filtering for paid field
     * @param sort          settings for sorting parameter (by views or event_date. Default sorting set by views)
     * @param rangeStart    start filtering date
     * @param rangeEnd      end filtering date
     * @param onlyAvailable filtering for only available events
     * @param from          the number of skipped records
     * @param size          the number of result list size
     * @param request       HttpServletRequest
     * @return List of FullShirtDto objects
     */
    List<EventShortDto> getEventsForPublicWithParams(String text, List<Integer> categories, Boolean paid,
                                                     SortType sort, LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                     Boolean onlyAvailable, int from, int size, HttpServletRequest request);

    /**
     * Get an existing event by id for public controller
     *
     * @param idEvent identification number of event
     * @param request HttServletRequest
     * @return EventFullDTO object
     * @throws NotFoundCustomException
     */
    EventFullDto getEventForPublicById(long idEvent, HttpServletRequest request) throws NotFoundCustomException;


}
