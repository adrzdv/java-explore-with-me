package ru.practicum.enw.service.event;

import ru.practicum.enw.exceptions.NotFoundCustomException;
import ru.practicum.enw.model.enums.SortType;
import ru.practicum.enw.model.event.*;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    EventFullDto add(NewEventDto event, long id) throws NotFoundCustomException;

    List<EventShortDto> getUsersEvents(long idUser, int from, int size);

    EventFullDto getUsersEvent(long idUser, long idEvent) throws NotFoundCustomException;

    EventFullDto updateUsersEvent(long userId, long eventId, UpdateEventUserRequest event)
            throws NotFoundCustomException;

    EventFullDto updateEventByAdmin(long id, UpdateEventAdminRequest event) throws NotFoundCustomException;

    List<EventFullDto> getAdminAllEvents(List<Long> users, List<String> states, List<Long> categories,
                                         LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);

    List<EventShortDto> getEventsForPublicWithParams(String text, List<Integer> categories, Boolean paid,
                                                     SortType sort, LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                     Boolean onlyAvailable, int from, int size);

    EventShortDto getEventForPublicById(long idEvent) throws NotFoundCustomException;
}
