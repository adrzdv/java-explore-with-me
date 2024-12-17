package ru.practicum.enw.controller.external;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.enw.exceptions.NotFoundCustomException;
import ru.practicum.enw.model.enums.SortType;
import ru.practicum.enw.model.event.EventShortDto;
import ru.practicum.enw.service.event.EventService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/events")
@AllArgsConstructor
public class EventPublicController {

    private final EventService eventService;

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
                                         @RequestParam(required = false, defaultValue = "VIEWS") SortType sort,
                                         @RequestParam(required = false, defaultValue = "0") int from,
                                         @RequestParam(required = false, defaultValue = "10") int size,
                                         HttpServletRequest request) {

        return eventService.getEventsForPublicWithParams(text, categories, paid, sort, rangeStart,
                rangeEnd, onlyAvailable, from, size);

    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventShortDto getEvent(@PathVariable long id, HttpServletRequest request) throws NotFoundCustomException {

        return eventService.getEventForPublicById(id, request);
    }
}
