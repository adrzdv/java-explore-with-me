package ru.practicum.enw.service.request;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.enw.exceptions.BadRequestCustomException;
import ru.practicum.enw.exceptions.ConflictCustomException;
import ru.practicum.enw.exceptions.NotFoundCustomException;
import ru.practicum.enw.model.entity.Event;
import ru.practicum.enw.model.entity.ParticipationRequest;
import ru.practicum.enw.model.enums.EventStates;
import ru.practicum.enw.model.enums.RequestStatus;
import ru.practicum.enw.model.mapper.custom.RequestMapper;
import ru.practicum.enw.model.request.EventRequestStatusUpdateRequest;
import ru.practicum.enw.model.request.EventRequestStatusUpdateResult;
import ru.practicum.enw.model.request.ParticipationRequestDto;
import ru.practicum.enw.repo.RequestRepo;
import ru.practicum.enw.repo.UserRepo;
import ru.practicum.enw.repo.EventRepo;
import ru.practicum.enw.service.user.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepo requestRepo;
    private final EventRepo eventRepo;
    private final UserRepo userRepo;
    private final UserService us;


    @Override
    @Transactional
    public ParticipationRequestDto addRequest(long idUser, long idEvent) throws NotFoundCustomException {

        Event event = eventRepo.findById(idEvent)
                .orElseThrow(() -> new NotFoundCustomException("Event with id=" + idEvent + " not found"));

        if (event.getInitiator().getId() == idUser) {
            throw new ConflictCustomException("Initiator can't be a requestor");
        }

        if (userRepo.findById(idUser).isEmpty()) {
            throw new NotFoundCustomException("User with id=" + idUser + " not found");
        }

        if (requestRepo.findByEventAndRequester(idEvent, idUser) != null) {
            throw new ConflictCustomException("User already made a request on the event");
        }

        if (!event.getState().equals(EventStates.PUBLISHED.name())) {
            throw new ConflictCustomException("Event wasn't published");
        }
        ParticipationRequest request = new ParticipationRequest();
        if (event.getParticipantLimit() != 0) {
            if (event.getConfirmedRequests() + 1 > event.getParticipantLimit()) {
                request.setStatus(RequestStatus.REJECTED.name());
                requestRepo.save(request);
                throw new ConflictCustomException("Limit of participants is over");
            }
        }

        request.setRequester(idUser);
        request.setEvent(event.getId());
        request.setCreated(LocalDateTime.now());


        if (event.getRequestModeration()) {
            if (event.getParticipantLimit() == 0) {
                request.setStatus(RequestStatus.CONFIRMED.name());
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                eventRepo.save(event);
            } else {
                request.setStatus(RequestStatus.PENDING.name());
            }
        } else {
            request.setStatus(RequestStatus.CONFIRMED.name());
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepo.save(event);
        }

        return RequestMapper.fromEntityToDto(requestRepo.save(request));
    }

    @Override
    public List<ParticipationRequestDto> getUsersRequests(long idUser) throws NotFoundCustomException {

        userRepo.findById(idUser)
                .orElseThrow(() -> new NotFoundCustomException("User with id=" + idUser + " not found"));

        return requestRepo.findAllByRequester(idUser).stream()
                .map(RequestMapper::fromEntityToDto)
                .toList();
    }

    @Override
    @Transactional
    public ParticipationRequestDto removeRequestFromEvent(long idUser, long idRequest) throws NotFoundCustomException {

        ParticipationRequest request = requestRepo.findById(idRequest)
                .orElseThrow(() -> new NotFoundCustomException("Request with id=" + idRequest + " not found"));

        if (request.getStatus().equals(RequestStatus.CANCELED.name()) ||
                request.getStatus().equals(RequestStatus.REJECTED.name())) {
            throw new ConflictCustomException("Request has already canceled");
        }

        if (!(request.getRequester() == idUser)) {
            throw new BadRequestCustomException("User is not an owner of request");
        }
        if (request.getStatus().equals(RequestStatus.CONFIRMED.name())) {
            request.setStatus(RequestStatus.CANCELED.name());
            Event event = eventRepo.findById(request.getEvent())
                    .orElseThrow(() -> new NotFoundCustomException("Event with id=" + request.getEvent() + " not found"));
            if (event.getConfirmedRequests() > 0) {
                event.setConfirmedRequests(event.getConfirmedRequests() - 1);
                eventRepo.save(event);
            } else {
                throw new ConflictCustomException("Conflict with confirmed requests");
            }
        } else {
            request.setStatus(RequestStatus.CANCELED.name());
        }

        return RequestMapper.fromEntityToDto(requestRepo.save(request));
    }

    @Override
    public List<ParticipationRequestDto> getAllRequestsOnEvent(long idUser, long idEvent)
            throws NotFoundCustomException {

        if (eventRepo.findEventByIdAndInitiatorId(idEvent, idUser) == null) {
            throw new NotFoundCustomException("Event with id=" + idEvent + " not found");
        }

        return requestRepo.findAllByEvent(idEvent).stream()
                .map(RequestMapper::fromEntityToDto)
                .toList();

    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateRequestsStatus(long idUser,
                                                               long idEvent,
                                                               EventRequestStatusUpdateRequest statusUpdater)
            throws NotFoundCustomException {

        Event event = eventRepo.findEventByIdAndInitiatorId(idEvent, idUser);

        if (!event.getRequestModeration() ||
                event.getParticipantLimit() == 0) {
            throw new ConflictCustomException("Event don't need to approve requests");
        }

        if (statusUpdater.getRequestIds().size() > event.getParticipantLimit() - event.getConfirmedRequests()) {
            throw new ConflictCustomException("The participant limit has been reached");
        }

        List<ParticipationRequestDto> requestsListForUpdate = requestRepo
                .findCurrentRequests(statusUpdater.getRequestIds())
                .stream()
                .map(RequestMapper::fromEntityToDto)
                .toList();

        if (requestsListForUpdate
                .stream()
                .noneMatch(requestDto -> requestDto.getStatus().equals(RequestStatus.PENDING.name()))) {
            throw new BadRequestCustomException("Request must have status PENDING");
        }

        int slot = event.getParticipantLimit() - event.getConfirmedRequests();


        List<ParticipationRequestDto> rejectedList;
        List<ParticipationRequestDto> confirmedList;

        if (statusUpdater.getStatus().equals(RequestStatus.REJECTED.name())) {
            for (ParticipationRequestDto request : requestsListForUpdate) {
                request.setStatus(RequestStatus.REJECTED.name());
            }
            rejectedList = new ArrayList<>(requestsListForUpdate);
            confirmedList = null;
        } else {
            int i = 0;
            while (i < requestsListForUpdate.size()) {

                if (i > slot) {
                    requestsListForUpdate.get(i).setStatus(RequestStatus.REJECTED.name());
                }

                requestsListForUpdate.get(i).setStatus(RequestStatus.CONFIRMED.name());
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);

                i++;
            }

            confirmedList = requestsListForUpdate
                    .stream()
                    .filter(requestDto -> requestDto.getStatus().equals(RequestStatus.CONFIRMED.name()))
                    .toList();

            rejectedList = requestsListForUpdate
                    .stream()
                    .filter(requestDto -> requestDto.getStatus().equals(RequestStatus.REJECTED.name()))
                    .toList();
        }

        requestRepo.saveAll(requestsListForUpdate.stream().map(RequestMapper::fromDtoToEntity).toList());
        eventRepo.save(event);

        return new EventRequestStatusUpdateResult(confirmedList, rejectedList);
    }


}
