package ru.practicum.enw.service.request;

import java.util.List;

import ru.practicum.enw.exceptions.NotFoundCustomException;
import ru.practicum.enw.model.request.EventRequestStatusUpdateRequest;
import ru.practicum.enw.model.request.EventRequestStatusUpdateResult;
import ru.practicum.enw.model.request.ParticipationRequestDto;

public interface RequestService {

    ParticipationRequestDto addRequest(long idUser, long idEvent) throws NotFoundCustomException;

    List<ParticipationRequestDto> getUsersRequests(long idUser) throws NotFoundCustomException;

    ParticipationRequestDto removeRequestFromEvent(long idUser, long idRequest) throws NotFoundCustomException;

    List<ParticipationRequestDto> getAllRequestsOnEvent(long idUser, long idEvent) throws NotFoundCustomException;

    EventRequestStatusUpdateResult updateRequestsStatus(long idUser,
                                                        long idEvent,
                                                        EventRequestStatusUpdateRequest statusUpdater)
            throws NotFoundCustomException;

}
