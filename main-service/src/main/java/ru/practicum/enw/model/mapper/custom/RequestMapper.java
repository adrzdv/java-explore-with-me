package ru.practicum.enw.model.mapper.custom;

import ru.practicum.enw.model.entity.ParticipationRequest;
import ru.practicum.enw.model.request.ParticipationRequestDto;

public class RequestMapper {

    public static ParticipationRequest fromDtoToEntity(ParticipationRequestDto request) {

        return ParticipationRequest.builder()
                .id(request.getId())
                .requester(request.getRequester())
                .created(request.getCreated())
                .event(request.getEvent())
                .status(request.getStatus())
                .build();

    }

    public static ParticipationRequestDto fromEntityToDto(ParticipationRequest request) {

        return ParticipationRequestDto.builder()
                .id(request.getId())
                .requester(request.getRequester())
                .created(request.getCreated())
                .event(request.getEvent())
                .status(request.getStatus())
                .build();
    }
}
