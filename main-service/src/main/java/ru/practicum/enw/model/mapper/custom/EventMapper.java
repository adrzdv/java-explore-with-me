package ru.practicum.enw.model.mapper.custom;

import org.springframework.stereotype.Component;
import ru.practicum.enw.model.entity.Category;
import ru.practicum.enw.model.entity.Event;
import ru.practicum.enw.model.event.EventFullDto;
import ru.practicum.enw.model.event.EventShortDto;
import ru.practicum.enw.model.event.NewEventDto;
import ru.practicum.enw.model.user.UserDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class EventMapper {

    public static Event fromNewDtoToEntity(NewEventDto newEvent, Category category, UserDto user) {

        Event event = new Event();
        event.setTitle(newEvent.getTitle());
        event.setDescription(newEvent.getDescription());
        event.setAnnotation(newEvent.getAnnotation());
        event.setCategory(category);
        LocalDateTime eventDate = LocalDateTime.parse(newEvent.getEventDate(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        event.setEventDate(eventDate);
        event.setInitiator(UserMapper.fromDtoToEntity(user));
        event.setLocation(newEvent.getLocation());
        event.setPaid(newEvent.getPaid());
        event.setParticipantLimit(newEvent.getParticipantLimit());
        event.setRequestModeration(newEvent.getRequestModeration());

        return event;
    }

    public static EventShortDto toShortDto(Event event) {

        return EventShortDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toDtoFromEntity(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests() != null ? event.getConfirmedRequests() : 0)
                .eventDate(event.getEventDate())
                .initiator(UserMapper.fromEntityToShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .views(event.getViews() != null ? event.getViews() :  0)
                .build();
    }

    public static EventFullDto fromEntityToEventFullDto(Event event) {

        return EventFullDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toDtoFromEntity(event.getCategory()))
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(UserMapper.fromEntityToDto(event.getInitiator()))
                .location(event.getLocation())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.getRequestModeration())
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .publishedOn(event.getPublishedOn())
                .state(event.getState())
                .views(event.getViews())
                .build();
    }

    public static Event fromFullDtoToEntity(EventFullDto eventFullDto) {

        return Event.builder()
                .id(eventFullDto.getId())
                .title(eventFullDto.getTitle())
                .annotation(eventFullDto.getAnnotation())
                .category(CategoryMapper.fromDtoToEntity(eventFullDto.getCategory()))
                .description(eventFullDto.getDescription())
                .eventDate(eventFullDto.getEventDate())
                .initiator(UserMapper.fromDtoToEntity(eventFullDto.getInitiator()))
                .location(eventFullDto.getLocation())
                .paid(eventFullDto.getPaid())
                .participantLimit(eventFullDto.getParticipantLimit())
                .requestModeration(eventFullDto.getRequestModeration())
                .confirmedRequests(eventFullDto.getConfirmedRequests())
                .createdOn(eventFullDto.getCreatedOn())
                .publishedOn(eventFullDto.getPublishedOn())
                .state(eventFullDto.getState())
                .views(eventFullDto.getViews())
                .build();
    }

}
