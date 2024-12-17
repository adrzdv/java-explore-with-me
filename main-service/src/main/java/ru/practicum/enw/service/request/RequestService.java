package ru.practicum.enw.service.request;

import java.util.List;

import ru.practicum.enw.exceptions.NotFoundCustomException;
import ru.practicum.enw.model.request.EventRequestStatusUpdateRequest;
import ru.practicum.enw.model.request.EventRequestStatusUpdateResult;
import ru.practicum.enw.model.request.ParticipationRequestDto;

public interface RequestService {

    /**
     * Add new request in event
     *
     * @param idUser  requestor identification number
     * @param idEvent event id
     * @return ParticipationRequestDto object
     * @throws NotFoundCustomException
     */
    ParticipationRequestDto addRequest(long idUser, long idEvent) throws NotFoundCustomException;

    /**
     * Get all requests from current user
     *
     * @param idUser identification number if user
     * @return List of ParticipationRequestDto objects
     * @throws NotFoundCustomException
     */
    List<ParticipationRequestDto> getUsersRequests(long idUser) throws NotFoundCustomException;

    /**
     * Cancel request from event
     *
     * @param idUser    requestor identification number
     * @param idRequest identification number of reuest
     * @return ParticipationRequestDto object
     * @throws NotFoundCustomException
     */
    ParticipationRequestDto removeRequestFromEvent(long idUser, long idRequest) throws NotFoundCustomException;

    /**
     * Get a list of ParticipationRequestDto for current event
     *
     * @param idUser  identification number of event's initiator
     * @param idEvent identification number of event
     * @return List of ParticipationRequestDto objects
     * @throws NotFoundCustomException
     */
    List<ParticipationRequestDto> getAllRequestsOnEvent(long idUser, long idEvent) throws NotFoundCustomException;

    /**
     * Update request status
     *
     * @param idUser        identification number of requestor
     * @param idEvent       event's identification number
     * @param statusUpdater EventRequestStatusUpdateRequest object
     * @return EventRequestStatusUpdateResult object
     * @throws NotFoundCustomException
     */
    EventRequestStatusUpdateResult updateRequestsStatus(long idUser,
                                                        long idEvent,
                                                        EventRequestStatusUpdateRequest statusUpdater)
            throws NotFoundCustomException;

}
