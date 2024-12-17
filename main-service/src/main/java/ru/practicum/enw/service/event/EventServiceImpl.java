package ru.practicum.enw.service.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import ru.practicum.enw.exceptions.BadRequestCustomException;
import ru.practicum.enw.exceptions.ConflictCustomException;
import ru.practicum.enw.exceptions.ForbiddenCustomException;
import ru.practicum.enw.exceptions.NotFoundCustomException;
import ru.practicum.enw.model.entity.Category;
import ru.practicum.enw.model.entity.Event;
import ru.practicum.enw.model.entity.LocationEwm;
import ru.practicum.enw.model.enums.SortType;
import ru.practicum.enw.model.event.*;
import ru.practicum.enw.model.mapper.mapstruct.EventMapperMapStruct;
import ru.practicum.enw.model.user.UserDto;
import ru.practicum.enw.repo.CategoryRepo;
import ru.practicum.enw.model.mapper.custom.EventMapper;
import ru.practicum.enw.repo.EventRepo;
import ru.practicum.enw.service.location.LocationService;
import ru.practicum.enw.service.user.UserService;
import ru.practicum.ewm.StatsClient;
import ru.practicum.statsdto.HitObjectDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static ru.practicum.enw.model.enums.EventStates.*;
import static ru.practicum.enw.model.enums.StateAction.*;

@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepo eventRepo;
    private final CategoryRepo categoryRepo;
    private final UserService userService;
    private final LocationService locationService;
    private final EventMapperMapStruct eventMapperMapStruct;


    private final StatsClient client;

    @Autowired
    public EventServiceImpl(EventRepo eventRepo,
                            CategoryRepo categoryRepo,
                            UserService userService,
                            LocationService locationService,
                            EventMapperMapStruct eventMapperMapStruct,
                            RestTemplate rest,
                            @Value("${stats-server.uri}") String statsServerString) {
        this.eventRepo = eventRepo;
        this.categoryRepo = categoryRepo;
        this.userService = userService;
        this.locationService = locationService;
        this.eventMapperMapStruct = eventMapperMapStruct;
        this.client = new StatsClient(statsServerString, rest);
    }

    @Override
    @Transactional
    public EventFullDto add(NewEventDto event, long id) throws NotFoundCustomException {

        UserDto initiator = userService.getById(id);
        if (initiator == null) {
            throw new NotFoundCustomException("User with id=" + id + " not found");
        }

        if (event.getParticipantLimit() != null) {
            if (event.getParticipantLimit() < 0) {
                throw new ConflictCustomException("Participant limit must be positive value");
            }
        }

        if (LocalDateTime.parse(event.getEventDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                .isBefore(LocalDateTime.now())) {
            throw new BadRequestCustomException("Date can't be in the past");
        }

        Category category = categoryRepo.findById(event.getCategory())
                .orElseThrow(() -> new NotFoundCustomException("Category is unknown"));

        Event newEvent = EventMapper.fromNewDtoToEntity(event, category, initiator);
        newEvent.setCreatedOn(LocalDateTime.now());
        newEvent.setState(PENDING.name());

        if (newEvent.getEventDate().isBefore(LocalDateTime.now().plusHours(2L))) {
            throw new BadRequestCustomException("Field: eventDate. Error: должно содержать дату, которая еще не " +
                    "наступила. Value: "
                    + newEvent.getEventDate());
        }

        return EventMapper.fromEntityToEventFullDto(eventRepo.save(newEvent));
    }

    @Override
    public List<EventShortDto> getUsersEvents(long idUser, int from, int size) {

        return eventRepo.getEventsWithParams(idUser, from, size).stream()
                .map(EventMapper::toShortDto)
                .toList();
    }

    @Override
    public EventFullDto getUsersEvent(long idUser, long idEvent) throws NotFoundCustomException {

        Event event = eventRepo.findEventByIdAndInitiatorId(idEvent, idUser);

        if (event == null) {
            throw new NotFoundCustomException("Event with id=" + idEvent + " not found");
        }

        return EventMapper.fromEntityToEventFullDto(event);
    }

    @Override
    @Transactional
    public EventFullDto updateUsersEvent(long idUser, long idEvent, UpdateEventUserRequest updEvent)
            throws NotFoundCustomException {

        if (updEvent.getEventDate() != null) {
            LocalDateTime updateEventDate = updEvent.getEventDate();
            if (updateEventDate.isBefore(LocalDateTime.now()) || !updateEventDate.isAfter(LocalDateTime.now()
                    .plusHours(2L))) {
                throw new BadRequestCustomException("Field: eventDate. Error: должно содержать дату, " +
                        "которая еще не наступила. " +
                        "Value: " + updEvent.getEventDate());
            }
        }

        if (updEvent.getParticipantLimit() != null) {
            if (updEvent.getParticipantLimit() < 0) {
                throw new ConflictCustomException("Participant limit must be positive value");
            }
        }

        Event event = eventRepo.findEventByIdAndInitiatorId(idEvent, idUser);
        Category category;

        if (event == null) {
            throw new NotFoundCustomException("Event with id=" + idEvent + "was not found");
        }

        if (updEvent.getCategory() == null) {
            category = event.getCategory();
        } else {
            category = categoryRepo.findById(updEvent.getCategory())
                    .orElseThrow(() -> new NotFoundCustomException("Category is unknown"));
        }

        if (!(event.getState().equals(PENDING.name()) ||
                event.getState().equals(CANCELED.name()))) {
            throw new ForbiddenCustomException("Only pending or canceled events can be changed");
        } else if (event.getState().equals(PUBLISHED.name())) {
            throw new BadRequestCustomException("Event can't be published");
        }

        LocationEwm location;

        if (updEvent.getLocation() != null) {
            if (!event.getLocation().equals(updEvent.getLocation())) {
                location = locationService.add(updEvent.getLocation());
                updEvent.setLocation(location);
            } else {
                updEvent.setLocation(event.getLocation());
            }
        }

        if (updEvent.getStateAction() != null) {
            if (updEvent.getStateAction().equals(CANCEL_REVIEW.name())) {
                event.setState(CANCELED.name());
            } else {
                event.setState(PENDING.name());
            }
        }

        eventMapperMapStruct.updateFullEventDtoFromNewEventDto(updEvent, event);
        event.setCategory(category);

        return EventMapper.fromEntityToEventFullDto(eventRepo.save(event));
    }

    @Override
    @Transactional
    public EventFullDto updateEventByAdmin(long id, UpdateEventAdminRequest event) throws NotFoundCustomException {

        Event eventOld = eventRepo.findById(id)
                .orElseThrow(() -> new NotFoundCustomException("Event with id=" + id + " not found"));

        if (event == null) {
            return EventMapper.fromEntityToEventFullDto(eventOld);
        }

        if (event.getParticipantLimit() != null) {
            if (event.getParticipantLimit() < 0) {
                throw new ConflictCustomException("Participant limit must be positive value");
            }
        }

        if (!eventOld.getEventDate().isAfter(LocalDateTime.now().plusHours(1L))) {
            throw new ConflictCustomException("The start date of the updated event must " +
                    "be no earlier than one hour from the publication date.");
        }

        if (event.getStateAction() != null) {
            if (event.getStateAction().equals(PUBLISH_EVENT.name()) &&
                    eventOld.getState().equals(PENDING.name())) {

                eventOld.setState(PUBLISHED.name());

            } else if (event.getStateAction().equals(REJECT_EVENT.name()) &&
                    !eventOld.getState().equals(PUBLISHED.name())) {

                eventOld.setState(CANCELED.name());

            } else {
                throw new ForbiddenCustomException("Event is not in PENDING state or already had PUBLISHED");
            }
        }
        Category category;
        if (event.getCategory() != null) {
            category = categoryRepo.findById(event.getCategory())
                    .orElseThrow(() -> new NotFoundCustomException("Category not found"));
        } else {
            category = eventOld.getCategory();
        }

        eventMapperMapStruct.updateFullEventDtoFromUpdateEventAdminRequest(event, eventOld);

        eventOld.setCategory(category);
        eventOld.setPublishedOn(LocalDateTime.now());

        return EventMapper.fromEntityToEventFullDto(eventRepo.save(eventOld));
    }

    @Override
    public List<EventFullDto> getAdminAllEvents(List<Long> users, List<String> states, List<Long> categories,
                                                LocalDateTime rangeStart,
                                                LocalDateTime rangeEnd, int from, int size) {

        if (rangeStart == null || rangeEnd == null) {
            return eventRepo.findAllEventsForAdminHavingParamsWithoutDates(users, states, categories, from, size).stream()
                    .map(EventMapper::fromEntityToEventFullDto)
                    .toList();
        }

        return eventRepo.findAllEventsForAdminHavingParams(users, states, categories, rangeStart, rangeEnd, from, size).stream()
                .map(EventMapper::fromEntityToEventFullDto)
                .toList();

    }


    @Override
    public List<EventShortDto> getEventsForPublicWithParams(String text, List<Integer> categories, Boolean paid,
                                                            SortType sort, LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                            Boolean onlyAvailable, int from, int size) {

        if (rangeEnd != null && (rangeEnd.isBefore(rangeStart) || (categories != null && categories.contains(0)))) {
            throw new BadRequestCustomException("Incorrect parameters in request");
        }

        String searchText;
        if (text != null) {
            searchText = text.toLowerCase();
        } else {
            searchText = null;
        }
        List<EventShortDto> res;

        if (rangeEnd != null || rangeStart != null) {
            res = eventRepo.getAllEventsByFiltersForPublic(searchText, categories, paid, onlyAvailable,
                            sort != null ? sort.name() : null,
                            rangeStart, rangeEnd, from, size)
                    .stream()
                    .map(EventMapper::toShortDto)
                    .toList();
        } else {
            res = eventRepo.getAllEventsFromCurrentDate(searchText, categories, paid, onlyAvailable,
                            sort != null ? sort.name() : null,
                            LocalDateTime.now(), from, size)
                    .stream()
                    .map(EventMapper::toShortDto)
                    .toList();
        }

        return res;
    }

    @Override
    @Transactional
    public EventFullDto getEventForPublicById(long idEvent, HttpServletRequest request) throws NotFoundCustomException {

        client.hitUri("ewm-main-service", request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());

        ObjectMapper mapper = new ObjectMapper();
        List<HitObjectDto> convertedBody;
        LocalDateTime startStats = LocalDateTime.now().minusYears(1L);
        LocalDateTime endStats = LocalDateTime.now().plusYears(1L);

        ResponseEntity<Object> resp = client.getStats(startStats, endStats, List.of(request.getRequestURI()), true);
        Object respBody = resp.getBody();

        convertedBody = mapper.convertValue(respBody, mapper.getTypeFactory().constructCollectionType(List.class, HitObjectDto.class));

        Event event = eventRepo.findById(idEvent)
                .orElseThrow(() -> new NotFoundCustomException("Event with id=" + idEvent + " not found"));

        if (!event.getState().equals(PUBLISHED.name())) {
            throw new NotFoundCustomException("Event must be published");
        }

        event.setViews(convertedBody.getFirst().getHits());

        eventRepo.save(event);

        return EventMapper.fromEntityToEventFullDto(event);
    }


}
