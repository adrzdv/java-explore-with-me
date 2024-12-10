package ru.practicum.enw.controller.external;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.practicum.enw.exceptions.NotFoundCustomException;
import ru.practicum.enw.model.enums.SortType;
import ru.practicum.enw.model.event.EventShortDto;
import ru.practicum.enw.service.event.EventService;
import ru.practicum.ewm.StatsClient;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/events")
public class EventPublicController {

    private final EventService eventService;
    private final StatsClient client;

    @Autowired
    public EventPublicController(EventService eventService, RestTemplate rest,
                     @Value("${stats-server.uri}") String statsServerString) {
        this.eventService = eventService;
        this.client = new StatsClient(statsServerString, rest);
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getEvents(@RequestParam(required = false) String text,
                                         @RequestParam(required = false) List<Integer> categories,
                                         @RequestParam(required = false) Boolean paid,
                                         @RequestParam(required = false)
                                         @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                         @RequestParam(required = false)
                                         @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                         @RequestParam(required = false) Boolean onlyAvailable,
                                         @RequestParam(required = false) SortType sort,
                                         @RequestParam(required = false, defaultValue = "0") int from,
                                         @RequestParam(required = false, defaultValue = "10") int size,
                                         HttpServletRequest request) {

        client.hitUri("ewm-main-service", request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());

        return eventService.getEventsForPublicWithParams(text, categories, paid, sort, rangeStart,
                rangeEnd, onlyAvailable, from, size);

    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventShortDto getEvent(@PathVariable long id, HttpServletRequest request) throws NotFoundCustomException {

        client.hitUri("ewm-main-service", request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());

        return eventService.getEventForPublicById(id);
    }
}
