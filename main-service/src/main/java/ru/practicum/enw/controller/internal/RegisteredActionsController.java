package ru.practicum.enw.controller.internal;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.enw.exceptions.NotFoundCustomException;
import ru.practicum.enw.model.comment.CommentDto;
import ru.practicum.enw.model.comment.NewCommentDto;
import ru.practicum.enw.model.event.*;
import ru.practicum.enw.model.request.EventRequestStatusUpdateRequest;
import ru.practicum.enw.model.request.EventRequestStatusUpdateResult;
import ru.practicum.enw.model.request.ParticipationRequestDto;
import ru.practicum.enw.service.comment.CommentService;
import ru.practicum.enw.service.event.EventService;
import ru.practicum.enw.service.location.LocationService;
import ru.practicum.enw.service.request.RequestService;

import java.util.List;

@RestController
@RequestMapping(value = "/users/{id}")
@AllArgsConstructor
public class RegisteredActionsController {

    private final EventService eventService;
    private final LocationService locationService;
    private final RequestService requestService;
    private final CommentService commentService;

    @PostMapping(value = "/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto add(@Valid @RequestBody NewEventDto event,
                            @PathVariable long id) throws NotFoundCustomException {

        locationService.add(event.getLocation());
        return eventService.add(event, id);
    }

    @GetMapping(value = "/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getEvents(@PathVariable long id,
                                         @RequestParam(defaultValue = "0") int from,
                                         @RequestParam(defaultValue = "10") int size) {

        return eventService.getUsersEvents(id, from, size);
    }

    @GetMapping(value = "/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEvent(@PathVariable long id,
                                 @PathVariable long eventId) throws NotFoundCustomException {

        return eventService.getUsersEvent(id, eventId);
    }

    @PatchMapping(value = "/events/{idEvent}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEvent(@PathVariable long id,
                                    @PathVariable long idEvent,
                                    @Valid @RequestBody UpdateEventUserRequest event) throws NotFoundCustomException {

        return eventService.updateUsersEvent(id, idEvent, event);
    }

    @PostMapping(value = "/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto sendRequestOnEvent(@PathVariable long id,
                                                      @RequestParam long eventId) throws NotFoundCustomException {
        return requestService.addRequest(id, eventId);
    }

    @GetMapping(value = "/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getAllUsersRequests(@PathVariable long id) throws NotFoundCustomException {

        return requestService.getUsersRequests(id);
    }

    @PatchMapping(value = "/requests/{idRequest}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public ParticipationRequestDto removeRequestFromEvent(@PathVariable long id,
                                                          @PathVariable long idRequest) throws NotFoundCustomException {

        return requestService.removeRequestFromEvent(id, idRequest);
    }

    @GetMapping(value = "/events/{idEvent}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getAllRequestsOnEvent(@PathVariable long id,
                                                               @PathVariable long idEvent) throws NotFoundCustomException {


        return requestService.getAllRequestsOnEvent(id, idEvent);
    }

    @PatchMapping(value = "/events/{idEvent}/requests")
    @ResponseStatus(HttpStatus.OK)
    public EventRequestStatusUpdateResult updateRequestsStatus(@PathVariable long id,
                                                               @PathVariable long idEvent,
                                                               @Valid @RequestBody EventRequestStatusUpdateRequest statusUpdater)
            throws NotFoundCustomException {

        return requestService.updateRequestsStatus(id, idEvent, statusUpdater);
    }

    @PostMapping(value = "/events/{idEvent}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto postComment(@PathVariable long id,
                                  @PathVariable long idEvent,
                                  @Valid @RequestBody NewCommentDto comment) throws NotFoundCustomException {

        return commentService.addComment(comment, idEvent, id);

    }

    @PatchMapping(value = "/events/{idEvent}/comment/{idComment}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto updateComment(@PathVariable long id,
                                    @PathVariable long idEvent,
                                    @PathVariable long idComment,
                                    @Valid @RequestBody NewCommentDto comment) throws NotFoundCustomException {

        return commentService.updateComment(comment, idEvent, id, idComment);

    }

    @DeleteMapping(value = "/events/{idEvent}/comment/{idComment}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteComment(@PathVariable long id,
                              @PathVariable long idEvent,
                              @PathVariable long idComment) throws NotFoundCustomException {

        commentService.deleteCommentByAuthor(id, idEvent, idComment);

    }

    @GetMapping(value = "/events/{idEvent}/comment")
    @ResponseStatus(HttpStatus.OK)
    public EventShortWithCommentDto getUsersEventWithComments(@PathVariable long id,
                                                              @PathVariable long idEvent,
                                                              @RequestParam(required = false, defaultValue = "10") int size,
                                                              @RequestParam(required = false, defaultValue = "0") int from) throws NotFoundCustomException {

        return commentService.getEventComments(idEvent, id, size, from);

    }

}
